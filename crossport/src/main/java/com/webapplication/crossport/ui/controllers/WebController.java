package com.webapplication.crossport.ui.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * Base controller charged to return home page
 *
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
 */
@Controller
public class WebController
{
    @GetMapping(value = {"/home", "/index.html"})
    public String redirect() {
        return "redirect:/";
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }
}
