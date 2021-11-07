package com.webapplication.crossport;

import com.webapplication.crossport.service.AuthService;
import com.webapplication.crossport.service.RequestType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class CrossportApplication extends SpringBootServletInitializer
{

    public static void main(String[] args){
        AuthService.getInstance().makeRequest(RequestType.LOGIN, "admin", "admin");
        //SpringApplication.run(CrossportApplication.class, args);
    }

}
