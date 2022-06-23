package by.itacademy.mail.scheduler.view.job;

import by.itacademy.mail.scheduler.model.Mail;
import by.itacademy.mail.scheduler.model.Report;
import by.itacademy.mail.scheduler.model.Schedule;
import by.itacademy.mail.scheduler.utils.JwtTokenUtil;
import by.itacademy.mail.scheduler.view.api.JobScheduler;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
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

@Log4j2
@Component
public class MailReportJob extends QuartzJobBean {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;
    @Autowired
    private JobScheduler jobScheduler;
    @Value("${urls.mail-service}")
    private String mailServiceUrl;


    @Override
    public void executeInternal(JobExecutionContext context) {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        JobKey jobKey = context.getJobDetail().getKey();
        Mail mail = (Mail) dataMap.get("mail");
        Schedule schedule = (Schedule) dataMap.get("schedule");
        Report report = mail.getSubject();
        report.insertReportInterval(schedule.getTimeUnit());
        UserDetails userDetails = (UserDetails) dataMap.get("user");
        log.info("Executing job {}", jobKey);
        try {
            String from = (String) report.getParams().get("from");
            String to = (String) report.getParams().get("to");
            log.info("Report interval: from {} to {}", from, to);
            sendMailRequest(mail, userDetails);
            log.info("Success. Next execution at: {}", context.getTrigger().getNextFireTime());
        } catch (Exception exc) {
            log.error("Error occurred during job {} execution: {}",
                    jobKey, exc.getMessage(), exc.getCause());
            jobScheduler.pause(jobKey);
        }
    }

    private void sendMailRequest(Mail mail, UserDetails userDetails) throws HttpClientErrorException {
        String url = mailServiceUrl + "/report";
        HttpHeaders headers = new HttpHeaders();
        String token = "Bearer " + JwtTokenUtil.generateAccessToken(userDetails);
        headers.add(HttpHeaders.AUTHORIZATION, token);
        HttpEntity<Mail> request = new HttpEntity<>(mail, headers);
        restTemplateBuilder.build().postForEntity(url, request, Void.class);
    }
}
