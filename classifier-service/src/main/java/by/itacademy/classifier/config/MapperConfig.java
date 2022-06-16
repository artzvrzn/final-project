package by.itacademy.classifier.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    private final ModelMapper mapper = new ModelMapper();

    @Bean
    public ModelMapper modelMapper() {
        return mapper;
    }
}
