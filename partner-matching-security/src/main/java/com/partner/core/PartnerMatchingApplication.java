package com.partner.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.partner.core",
        "com.partner.security" 
})
public class PartnerMatchingApplication {
    public static void main(String[] args) {
        SpringApplication.run(PartnerMatchingApplication.class, args);
    }
}