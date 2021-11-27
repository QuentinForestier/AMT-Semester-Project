package com.webapplication.crossport.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class WebController
{

    @GetMapping(value={"/", "/home", "/index.html"})
    public String home(@RequestParam(name="name", required=false, defaultValue="visitor") String name, Model model) {
        model.addAttribute("name", name);

        return "index";
    }

    @GetMapping("/login")
    public String login(){

        return "login";
    }
}
