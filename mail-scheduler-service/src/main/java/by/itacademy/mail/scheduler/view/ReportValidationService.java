package by.itacademy.mail.scheduler.view;

import by.itacademy.mail.scheduler.exception.ResponseException;
import by.itacademy.mail.scheduler.model.Mail;
import by.itacademy.mail.scheduler.model.Report;
import by.itacademy.mail.scheduler.model.ScheduledMail;
import by.itacademy.mail.scheduler.model.error.ResponseError;
import by.itacademy.mail.scheduler.utils.JwtTokenUtil;
import by.itacademy.mail.scheduler.view.api.ValidationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@Service
public class ReportValidationService implements ValidationService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper mapper;
    @Value("${urls.mail-service}")
    private String mailServiceUrl;

    @Override
    public void validate(ScheduledMail scheduledMail) {
        sendValidationRequest(scheduledMail);
    }

    public void sendValidationRequest(ScheduledMail scheduledMail) {
        Report report = scheduledMail.getMail().getSubject();
        report.insertReportInterval(scheduledMail.getSchedule().getTimeUnit());
        String url = mailServiceUrl + "/validate/report/" + report.getType();
        try {
            HttpEntity<Mail> httpEntity = new HttpEntity<>(scheduledMail.getMail(), buildHeaders(serviceToken()));
            log.info("Knocking on {} body {}", url, report.getParams());
            restTemplate.exchange(url, HttpMethod.POST, httpEntity, Void.class);
        } catch (HttpStatusCodeException e) {
            log.error("Validation not passed: {}", e.getMessage());
            throw handleException(e);
        }
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

    private static String serviceToken() {
        UserDetails userDetails = User.builder()
                .roles("ADMIN")
                .username("mail-scheduler-service")
                .password("mail-scheduler-service")
                .build();
        return "Bearer " + JwtTokenUtil.generateAccessToken(userDetails);
    }
}
