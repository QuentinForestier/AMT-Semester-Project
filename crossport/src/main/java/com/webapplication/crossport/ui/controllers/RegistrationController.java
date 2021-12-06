package com.webapplication.crossport.ui.controllers;

import com.webapplication.crossport.ui.formdata.MemberRegistrationData;
import com.webapplication.crossport.domain.services.MemberService;
import com.webapplication.crossport.config.security.exception.RegistrationException;
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

/**
 *
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
 */
@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private MemberService memberService;

    @GetMapping
    public String getRegisterPage(Model model){
        model.addAttribute("memberRegistrationData", new MemberRegistrationData());
        return "register";
    }

    @PostMapping
    public String performRegistration(final @Valid MemberRegistrationData memberRegistrationData, final BindingResult bindingResult, final Model model){

        if(bindingResult.hasErrors()){
            model.addAttribute("memberRegistrationData", memberRegistrationData);
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
            model.addAttribute("memberRegistrationData", memberRegistrationData);
            return "register";
        }

        return "redirect:/login";
    }
}
