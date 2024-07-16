package com.github.meditrust;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MediTrustApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(MediTrustApiApplication.class, args);
    }
}
