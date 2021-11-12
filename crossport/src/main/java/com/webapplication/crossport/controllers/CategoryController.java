package com.webapplication.crossport.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.webapplication.crossport.models.Category;
import com.webapplication.crossport.service.CategoryService;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String listCategories(Model model) {
        model.addAttribute("listCategories", categoryService.getAllCategories());
        model.addAttribute("category", new Category());
        return "categories";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute("category") Category category) {
        categoryService.saveCategory(category);
        return "redirect:/categories";
    }

    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable(value = "id") Integer id) {
        this.categoryService.deleteCategory(id);
        return "redirect:/categories";
    }

}
