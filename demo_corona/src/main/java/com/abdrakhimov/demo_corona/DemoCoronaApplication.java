package com.abdrakhimov.demo_corona;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DemoCoronaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoCoronaApplication.class, args);
    }

}
