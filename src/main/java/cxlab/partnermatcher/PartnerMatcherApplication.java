package cxlab.partnermatcher;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cxlab.partnermatcher.mapper")
public class PartnerMatcherApplication {

    public static void main(String[] args) {
        SpringApplication.run(PartnerMatcherApplication.class, args);
    }
}