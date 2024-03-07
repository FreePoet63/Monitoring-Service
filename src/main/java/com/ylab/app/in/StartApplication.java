package com.ylab.app.in;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * StartApplication class to initialize and run the meter reading service application.
 *
 * @author razlivinsky
 * @since 24.01.2024
 */
@SpringBootApplication
@ComponentScan("com.ylab.app")
public class StartApplication {
    public static void main(String[] args) {
        SpringApplication.run(StartApplication.class, args);
    }
}

    