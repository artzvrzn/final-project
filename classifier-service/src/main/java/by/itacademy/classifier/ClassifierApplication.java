package by.itacademy.classifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "by.itacademy.classifier.dao")
public class ClassifierApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClassifierApplication.class, args);
    }
}