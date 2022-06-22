package by.itacademy.account.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "by.itacademy.account.scheduler.dao")
public class AccountSchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountSchedulerApplication.class, args);
    }
}
