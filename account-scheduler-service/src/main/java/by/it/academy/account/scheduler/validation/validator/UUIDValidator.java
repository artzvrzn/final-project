package by.it.academy.account.scheduler.validation.validator;

import by.it.academy.account.scheduler.validation.anno.Exist;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

@Log4j2
public class UUIDValidator implements ConstraintValidator<Exist, UUID> {

    private final RestTemplate restTemplate;
    private Exist.ServiceType service;
    @Value("${urls.classifier-service}")
    private String classifierUrl;
    @Value("${urls.account-service}")
    private String accountServiceUrl;

    public UUIDValidator(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void initialize(Exist annotation) {
        service = annotation.value();
    }

    @Override
    public boolean isValid(UUID id, ConstraintValidatorContext context) {
        if (id == null) {
            return true;
        }
        ResponseEntity<?> responseEntity;
        try {
            String url = getUrl(id, service);
            log.info(String.format("Knocking on %s", url));
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, Void.class);
        } catch (HttpClientErrorException exc) {
            log.error(exc.getMessage());
            if (exc.getStatusCode().equals(HttpStatus.FORBIDDEN)) {
                throw new AccessDeniedException("access is forbidden");
            }
            if (exc.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                throw new SecurityException("unauthorized");
            }
            return false;
        }
        return responseEntity.getStatusCode().equals(HttpStatus.OK);
    }

    private String getUrl(UUID id, Exist.ServiceType serviceType) {
        switch (serviceType) {
            case ACCOUNT:
                return accountServiceUrl + "/" + id;
            case CATEGORY:
                return classifierUrl + "/operation/category/" + id;
            case CURRENCY:
                return classifierUrl + "/currency/" + id;
            default:
                throw new IllegalStateException("Wrong serviceType " + serviceType);
        }
    }
}
