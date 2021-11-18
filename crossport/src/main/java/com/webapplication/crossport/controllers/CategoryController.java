package com.webapplication.crossport.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.webapplication.crossport.models.Category;
import com.webapplication.crossport.service.CategoryService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/categories")
    public String listCategories(Model model) {
        model.addAttribute("listCategories", categoryService.getAllCategories());

        if(!model.containsAttribute("category")) {
            model.addAttribute("category", new Category());
        }

        return "categories";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute("category") Category category,
                               final BindingResult bindingResult,
                               RedirectAttributes attr,
                               HttpSession session) {
        List<Category> cat = categoryService.getAllCategories()
                        .stream()
                        .filter(c -> c.getName().equals(category.getName()))
                        .collect(Collectors.toList());

        if (cat.isEmpty()) {
            categoryService.saveCategory(category);
        } else {
            bindingResult.addError(new ObjectError("Category error",
                    "This category already exists"));
            attr.addFlashAttribute("org.springframework.validation.BindingResult.createCategory", bindingResult);
            attr.addFlashAttribute("createCategory", category);
            return "redirect:/categories";
        }

        return "redirect:/categories";
    }



    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable(value = "id") Integer id, final BindingResult bindingResult) {
        if (this.categoryService.getCategoryById(id).getArticles().isEmpty()) {
            this.categoryService.deleteCategory(id);
        } else {
            bindingResult.addError(new ObjectError("Category error",
                    "You cannot delete this category as it has article bound."));
        }

        return "redirect:/categories";
    }

}
