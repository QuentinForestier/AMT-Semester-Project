package com.webapplication.crossport.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * Base controller charged to return home page
 * @author Herzig Melvyn
 */
@Controller
public class WebController
{

    @GetMapping(value = {"/", "/index.html"})
    public String redirect() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home() {
        return "index";
    }
}
