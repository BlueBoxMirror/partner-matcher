package com.example.partnermatchingcore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.partnermatchingcore.mapper")
public class PartnermatchingcoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(PartnermatchingcoreApplication.class, args);
    }
}
