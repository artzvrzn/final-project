package by.itacademy.telegram.view;

import by.itacademy.telegram.model.Authentication;
import by.itacademy.telegram.view.api.AuthenticationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Log4j2
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final RestTemplate restTemplate;
    @Value("${urls.user-service}")
    private String userServiceUrl;

    public AuthenticationServiceImpl(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @Override
    public String getToken(Authentication authentication) throws SecurityException {
        try {
            return "Bearer " + restTemplate.postForObject(userServiceUrl, authentication, String.class);
        } catch (HttpStatusCodeException exc) {
            log.error("user service could not authenticate user: {}", exc.getMessage());
            throw new SecurityException("Wrong password or username");
        }
    }
}
