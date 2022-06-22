package by.itacademy.report.config;

import by.itacademy.report.utils.JwtTokenUtil;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder.build();
        restTemplate.setInterceptors(Collections.singletonList(new JwtTokenSpringContextInjectInterceptor()));
        return restTemplate;
    }

    private static class JwtTokenSpringContextInjectInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept
                (HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            UserDetails userDetails =
                    (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String jwtToken = "Bearer " + JwtTokenUtil.generateAccessToken(userDetails);
            request.getHeaders().add(HttpHeaders.AUTHORIZATION, jwtToken);
            return execution.execute(request, body);
        }
    }
}
