package by.it.academy.account.scheduler.view.jobs;

import by.it.academy.account.scheduler.model.Operation;
import by.it.academy.account.scheduler.utils.JwtTokenUtil;
import by.it.academy.account.scheduler.view.api.JobScheduler;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;

@Component
@Log4j2
public class ScheduledOperationJob extends QuartzJobBean {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;
    @Autowired
    private JobScheduler jobScheduler;
    @Value("${urls.account-service}")
    private String accountServiceUrl;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        JobKey jobKey = context.getJobDetail().getKey();
        Operation operation = (Operation) dataMap.get("operation");
        operation.setDate(LocalDate.now(ZoneOffset.UTC));
        UserDetails userDetails = (UserDetails) dataMap.get("user");
        log.info("Executing job {}", jobKey);
        try {
            sendRequest(operation, userDetails);
            if (operation.getValue().equals(new BigDecimal(0))) {
                throw new Exception("test");
            }
            log.info("Success. Next execution at: {}", context.getTrigger().getNextFireTime());
        } catch (Exception exc) {
            log.error("Error occurred during job {} execution: {}",
                    jobKey, exc.getMessage(), exc.getCause());
            jobScheduler.pause(jobKey);
        }
    }

    private void sendRequest(Operation operation, UserDetails userDetails) throws HttpClientErrorException {
        String url = accountServiceUrl + "/" + operation.getAccount() + "/operation/";
        HttpHeaders headers = new HttpHeaders();
        String token = "Bearer " + JwtTokenUtil.generateAccessToken(userDetails);
        headers.add(HttpHeaders.AUTHORIZATION, token);
        HttpEntity<Operation> request = new HttpEntity<>(operation, headers);
        operation.setAccount(null); // to exclude field from json
        restTemplateBuilder.build().postForEntity(url, request, Void.class);
    }
}
