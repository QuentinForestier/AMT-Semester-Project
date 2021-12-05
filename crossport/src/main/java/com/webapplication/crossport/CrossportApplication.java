package com.webapplication.crossport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@ServletComponentScan
@SpringBootApplication
public class CrossportApplication extends SpringBootServletInitializer
{
    public static void main(String[] args){

        // Start
        SpringApplication.run(CrossportApplication.class, args);
    }
}
