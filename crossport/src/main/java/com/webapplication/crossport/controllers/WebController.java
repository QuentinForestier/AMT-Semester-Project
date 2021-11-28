package com.webapplication.crossport.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Base controller charged to return home page
 * @author Herzig Melvyn
 */
@Controller
public class WebController
{

    @GetMapping(value={"/", "/home", "/index.html"})
    public String home(@RequestParam(name="name", required=false, defaultValue="visitor") String name, Model model) {
        model.addAttribute("name", name);

        return "index";
    }
}
