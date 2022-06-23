package by.itacademy.mail.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "by.itacademy.mail.scheduler.dao")
public class MailSchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MailSchedulerApplication.class, args);
    }

}
