package by.it.academy.report.config;

import by.it.academy.report.converter.StringMillisToLocalDateConverter;
import by.it.academy.report.converter.StringMillisToLocalDateTimeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringMillisToLocalDateTimeConverter());
        registry.addConverter(new StringMillisToLocalDateConverter());
    }
}
