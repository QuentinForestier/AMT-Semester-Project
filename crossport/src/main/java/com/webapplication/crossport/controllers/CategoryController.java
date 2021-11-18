package com.webapplication.crossport.controllers;

import com.webapplication.crossport.models.services.CategoryService;
import com.webapplication.crossport.service.CategoryData;
import com.webapplication.crossport.service.MemberRegistrationData;
import com.webapplication.crossport.service.exception.RegistrationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import com.webapplication.crossport.models.Category;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String listCategories(Model model) {

        model.addAttribute("listCategories", categoryService.getAllCategories());
        model.addAttribute("categoryData", new CategoryData());

        return "categories";
    }

    @PostMapping("/categories")
    public String saveCategory(final @Valid CategoryData categoryData, final BindingResult bindingResult, final Model model) {

        if(bindingResult.hasErrors()){
            model.addAttribute("categoryForm", categoryData);
            return "categories";
        }


        // Checking if already exists
        Category sameCategory = categoryService.getFirstByName(categoryData.getCategoryName());

        if (sameCategory != null){ // It already exists
            bindingResult.addError(new ObjectError("globalError", "Same category already exists."));
            model.addAttribute("categoryForm", categoryData);
        }
        else { // No category with the same name
            Category category = new Category();
            category.setName(categoryData.getCategoryName());
            categoryService.saveCategory(category);
        }

        model.addAttribute("listCategories", categoryService.getAllCategories());
        return "categories";
    }


    @GetMapping("/deleteCategory")
    public String deleteCategory(@RequestParam(value = "id") Integer id, RedirectAttributes redir) {

        boolean error = false;

        Category cat = categoryService.getCategoryById(id);

        if (cat.getArticles().isEmpty()) {
            categoryService.deleteCategory(id);
        } else {
            error = true;
            String delError = "You cannot delete this category as it has article bound.";
            redir.addFlashAttribute("delError", delError);
        }
        redir.addFlashAttribute("listCategories", categoryService.getAllCategories());
        redir.addFlashAttribute("categoryData", new CategoryData());

        return error ? "redirect:/categories?error=" + cat.getName() : "redirect:/categories";
    }

}
