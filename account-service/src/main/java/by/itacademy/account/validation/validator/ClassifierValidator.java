package by.itacademy.account.validation.validator;

import by.itacademy.account.validation.anno.Exist;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

@Log4j2
public class ClassifierValidator implements ConstraintValidator<Exist, UUID> {

    private final RestTemplate restTemplate;
    private Exist.Classifier classifierType;
    @Value("${urls.classifier-service}")
    private String classifierUrl;

    public ClassifierValidator(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void initialize(Exist annotation) {
        classifierType = annotation.value();
    }

    @Override
    public boolean isValid(UUID id, ConstraintValidatorContext context) {
        if (id == null) {
            return true;
        }
        ResponseEntity<?> responseEntity;
        try {
            String url = classifierUrl + classifierType.getUrl() + id;
            log.info(String.format("Knocking on %s", url));
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, Void.class);
        } catch (HttpClientErrorException exc) {
            log.error(exc.getMessage(), exc.getCause());
            return false;
        }
        return responseEntity.getStatusCode().equals(HttpStatus.OK);
    }
}
