package com.ravi.waterlilly.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Application configuration class.
@Configuration
public class AppConfig {

    //ModelMapper bean for object mapping.
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setSkipNullEnabled(true); // Skip null values
        return modelMapper;
    }
}
