package com.webapplication.crossport.controllers;

import com.webapplication.crossport.models.Article;
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

    // TODO DPO - C'est du détail, mais à moins que vous fassiez une énorme app, un contrôleur n'a en général
    //  que peu d'endpoint et pour une question de lisibilité le nom de vos méthodes n'ont pas besoin de contenir le nom de l'objet.
    //  Exemple pour chaque contrôleur:
    //  * GET -> findOne, findMany, findALl, ..
    //  * POST -> create
    //  * PATCH/PUT -> update, merge ou overwrite
    //  * DELETE -> delete (si vous souhaitez être fancy, vous pouvez mettre destroy ou wipe ^^)
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

    // TODO DPO - Soyez consistent sur le nom de vos endpoints. Dans le contrôleur "Registration", vous avez un exemple
    //  de comment nommer tous vos endpoint en plaçant @RequestMapping sur la classe.
    //  Exemple:
    //  * /categories -> GET, POST, PATCH, PUT et DELETE
    //  * /categories/{id} -> GET, POST, PATCH, PUT et DELETE
    //  ...
    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable(value = "id") Integer id, @RequestParam(value = "confirm", defaultValue = "false") boolean confirm, RedirectAttributes redir) {

        // boolean error = false;

        Category cat = categoryService.getCategoryById(id);

        if (cat.getArticles().isEmpty() || confirm) {
            categoryService.deleteCategory(id);
        } else {
            // error = true;
            String delError = "You cannot delete this category as it has articles bound.";
            redir.addFlashAttribute("delError", delError);

            return "redirect:/category/" + id;
        }
        redir.addFlashAttribute("listCategories", categoryService.getAllCategories());
        redir.addFlashAttribute("categoryData", new CategoryData());

        // return error ? "redirect:/categories?error=" + cat.getName() : "redirect:/categories";
        return "redirect:/categories";
    }

}
