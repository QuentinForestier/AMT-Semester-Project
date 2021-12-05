package com.webapplication.crossport;

import com.webapplication.crossport.controllers.ArticleController;
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
        ArticleController.uploadDir = appProps.getProperty("image_path");

        // Start
        SpringApplication.run(CrossportApplication.class, args);
    }
}
