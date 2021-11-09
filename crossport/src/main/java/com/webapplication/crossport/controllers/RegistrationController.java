package com.webapplication.crossport.controllers;

import com.webapplication.crossport.service.MemberRegistrationData;
import com.webapplication.crossport.service.MemberService;
import com.webapplication.crossport.service.exception.RegistrationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private MemberService memberService;

    @GetMapping
    public String register(Model model){
        model.addAttribute("memberRegistrationData", new MemberRegistrationData());
        return "register";
    }

    @PostMapping
    public String userRegistration(final @Valid MemberRegistrationData memberRegistrationData, final BindingResult bindingResult, final Model model){

        if(bindingResult.hasErrors()){
            model.addAttribute("registrationForm", memberRegistrationData);
            return "register";
        }

        try {
            memberService.register(memberRegistrationData);
        } catch (RegistrationException e){

            List<String> errors = e.getErrors();

            for(String error : errors) {
                bindingResult.addError(new ObjectError("globalError", error));
            }

            // Refill
            model.addAttribute("registrationForm", memberRegistrationData);
            return "register";
        }

        return "redirect:/login";
    }
}
