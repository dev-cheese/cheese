package com.cheese;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringServerApplication {

    public static void main(String[] args) {
        System.out.println(1111);
        SpringApplication.run(SpringServerApplication.class, args);
    }
}
