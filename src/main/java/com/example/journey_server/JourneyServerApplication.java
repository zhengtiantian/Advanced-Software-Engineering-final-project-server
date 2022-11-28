package com.example.journey_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example"})
public class JourneyServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JourneyServerApplication.class, args);
    }

}
