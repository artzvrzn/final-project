package by.it.academy.report.validation.validator;

import by.it.academy.report.controller.advice.error.Violation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Log4j2
@Component
public class CategoriesValidator extends UUIDsValidator {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${urls.classifier-service}")
    private String classifierServiceUrl;

    public CategoriesValidator() {
        super("categories");
    }

    public void sendRequest(UUID id, List<Violation> violations) {
        String url = classifierServiceUrl + "/operation/category/" + id;
        try {
            log.info("Knocking on {}", url);
            restTemplate.exchange(url, HttpMethod.GET, null, Void.class);
        } catch (HttpStatusCodeException exc) {
            log.error(exc.getMessage(), exc.getCause());
            violations.add(resolveResponse(id, exc.getStatusCode()));
        }
    }

    private Violation resolveResponse(UUID id, HttpStatus status) {
        switch (status) {
            case FORBIDDEN:
                throw new AccessDeniedException("Access denied");
            case UNAUTHORIZED:
                throw new SecurityException("Unauthorized access");
            default:
                return new Violation(fieldKey, id + " doesn't exist");
        }
    }
}
