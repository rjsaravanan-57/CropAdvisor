package com.example.CropAdvisor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CropAdvisorApplication {

    public static void main(String[] args) {

        SpringApplication.run(
                CropAdvisorApplication.class,
                args
        );
    }
}