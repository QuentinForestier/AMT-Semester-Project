package com.webapplication.crossport.controllers;

import com.webapplication.crossport.models.services.ArticleService;
import com.webapplication.crossport.models.services.CategoryService;
import com.webapplication.crossport.service.CategoryData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import com.webapplication.crossport.models.Category;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleService articleService;

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

    @GetMapping("/category/{id}")
    public String getCategoryId(@PathVariable(value = "id") Integer id, Model model) {
        Category category = categoryService.getCategoryById(id);

        model.addAttribute("category", category);
        return "category";
    }


    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable(value = "id") Integer id,
                                 @RequestParam(value = "confirm", defaultValue = "false") boolean confirm,
                                 RedirectAttributes redir) {
        Category cat;
        try {
            cat = categoryService.getCategoryById(id);
        } catch (RuntimeException e) {
            return "redirect:/categories";
        }

        if (cat.getArticles().isEmpty() || confirm) {
            categoryService.deleteCategory(id);
        } else {
            String delError = "You cannot delete this category as it has articles bound.";
            redir.addFlashAttribute("delError", delError);
            return "redirect:/category/" + id;
        }
        redir.addFlashAttribute("listCategories", categoryService.getAllCategories());
        redir.addFlashAttribute("categoryData", new CategoryData());
        return "redirect:/categories";
    }

    @GetMapping("/deleteCategoryFromArticle/{idCategory}/{idArticle}")
    public String deleteCategoryFromArticle(@PathVariable(value = "idCategory") Integer idCategory,
                                            @PathVariable(value = "idArticle") Integer idArticle,
                                            RedirectAttributes redir) {

        //redir.addFlashAttribute("delError", delError);
        Category category;
        try {
            category = categoryService.getCategoryById(idCategory);
        } catch (RuntimeException e) {
            String delError = "An error occured when we tried to find the category selected";
            redir.addFlashAttribute("delError", delError);
            return "redirect:/category/" + idCategory;
        }

        try {
            articleService.deleteACategory(idArticle, category);
        } catch (RuntimeException e) {
            String delError = "An error occured when we tried to delete the category from the article selected";
            redir.addFlashAttribute("delError", delError);
            return "redirect:/category/" + idCategory;
        }

        return "redirect:/category/" + idCategory;
    }

}
