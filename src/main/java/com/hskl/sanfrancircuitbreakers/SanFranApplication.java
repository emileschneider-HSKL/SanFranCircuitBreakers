package com.hskl.sanfrancircuitbreakers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SanFranApplication {

    public static void main(String[] args) {
        SpringApplication.run(SanFranApplication.class, args);
    }

}
