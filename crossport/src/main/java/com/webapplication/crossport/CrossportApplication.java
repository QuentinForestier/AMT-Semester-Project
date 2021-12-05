package com.webapplication.crossport;

import com.webapplication.crossport.config.filter.JWTFilter;
import com.webapplication.crossport.controllers.ArticleController;
import com.webapplication.crossport.service.AuthService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@ServletComponentScan
@SpringBootApplication
public class CrossportApplication extends SpringBootServletInitializer
{
    public static void main(String[] args){

        // Setting up image repository
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "custom.properties";
        appConfigPath = appConfigPath.replace("%20", " ");

        Properties appProps = new Properties();
        try {
            appProps.load(new FileInputStream(appConfigPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JWTFilter.secret = appProps.getProperty("secret");
        AuthService.address = appProps.getProperty("address");
        String temp = appProps.getProperty("port");
        AuthService.port = Integer.parseInt(temp);

        // Start
        SpringApplication.run(CrossportApplication.class, args);
    }
}
