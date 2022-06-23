package by.itacademy.mail.view;

import by.itacademy.mail.exception.ResponseException;
import by.itacademy.mail.model.Report;
import by.itacademy.mail.model.error.ResponseError;
import by.itacademy.mail.utils.JwtTokenUtil;
import by.itacademy.mail.view.api.CommunicatorService;
import by.itacademy.mail.view.api.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@Service
public class ReportServiceCommunicator implements CommunicatorService<Report> {

    private final RestTemplate restTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper mapper;
    @Value("${urls.report-service}")
    private String reportServiceUrl;

    public ReportServiceCommunicator(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @Override
    public UUID postRequest(Report report) {
        String url = reportServiceUrl + "report/" + report.getType();
        try {
            ResponseEntity<?> responseEntity = postReport(url, report.getParams());
            HttpHeaders headers = responseEntity.getHeaders();
            List<?> idList = headers.get("id");
            if (idList == null || idList.isEmpty()) {
                throw new IllegalStateException("Report service did not return report id");
            }
            return UUID.fromString((String) idList.get(0));
        } catch (HttpStatusCodeException e) {
            log.error("Post request has returned an error: {}", e.getMessage());
            throw handleException(e);
        }
    }

    public void sendValidationRequest(Report report) {
        String url = reportServiceUrl + "validate/" + report.getType();
        try {
            HttpEntity<Object> httpEntity = new HttpEntity<>(report.getParams(), buildHeaders(userToken()));
            log.info("Knocking on {} body {}", url, report.getParams());
            restTemplate.exchange(url, HttpMethod.POST, httpEntity, Void.class);
        } catch (HttpStatusCodeException e) {
            log.error("Validation not passed: {}", e.getMessage());
            throw handleException(e);
        }
    }

    @Override
    public boolean isAvailable(UUID id) {
        String url = reportServiceUrl + "account/" + id + "/export";
        HttpEntity<Object> httpEntity = new HttpEntity<>(buildHeaders(serviceToken()));
        log.info("Knocking on {}: ", url);
        ResponseEntity<Void> request = restTemplate.exchange(url, HttpMethod.HEAD, httpEntity, Void.class);
        return request.getStatusCode().equals(HttpStatus.OK);
    }

    private ResponseEntity<?> postReport(String url, Map<String, Object> params) {
        HttpEntity<Object> httpEntity = new HttpEntity<>(params, buildHeaders(userToken()));
        log.info("Knocking on {} body {}", url, params);
        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, Void.class);
    }

    private HttpHeaders buildHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, token);
        return headers;
    }

    private RuntimeException handleException(HttpStatusCodeException e) {
        HttpStatus httpStatus = e.getStatusCode();
        switch (httpStatus) {
            case FORBIDDEN:
                log.info(e.getMessage());
                return new AccessDeniedException("Access forbidden for this user", e);
            case UNAUTHORIZED:
                log.info(e.getMessage());
                return new SecurityException("Unauthorized access", e);
            default:
                ResponseError error = handleResponse(e);
                return new ResponseException(
                        "Report service has returned an error: " + e.getMessage(), error);
        }
    }

    private ResponseError handleResponse(HttpStatusCodeException exception) {
        if (!exception.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
            throw exception;
        }
        Pattern pattern = Pattern.compile("(\\{\".*\\w?})");
        Matcher matcher = pattern.matcher(exception.getResponseBodyAsString());
        try {
            while (matcher.find()) {
                return mapper.readValue(matcher.group(0), ResponseError.class);
            }
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
        return null;
    }

    private String userToken() {
        UserDetails userDetails = userService.getUserDetails();
        return "Bearer " + JwtTokenUtil.generateAccessToken(userDetails);
    }

    private static String serviceToken() {
        UserDetails userDetails = User.builder()
                .roles("ADMIN")
                .username("mail-service")
                .password("mail-service")
                .build();
        return "Bearer " + JwtTokenUtil.generateAccessToken(userDetails);
    }
}
