package com.ravi.waterlilly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VillaWaterLillyBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(VillaWaterLillyBackendApplication.class, args);
    }

}
