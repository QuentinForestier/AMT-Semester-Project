package com.webapplication.crossport.ui.controllers;

import com.webapplication.crossport.infra.models.Article;
import com.webapplication.crossport.infra.models.Category;
import com.webapplication.crossport.domain.services.ArticleService;
import com.webapplication.crossport.domain.services.CategoryService;
import com.webapplication.crossport.ui.dto.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
import java.util.List;

/**
 * Controller charged to manage categories form admin view
 *
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
 */
@Controller
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleService articleService;

    @GetMapping("")
    public String getAll(Model model) {
        model.addAttribute("listCategories", categoryService.getAllCategories());
        model.addAttribute("categoryDTO", new CategoryDTO());

        return "categories";
    }

    @PostMapping("")
    public String add(final @Valid CategoryDTO categoryDTO, final BindingResult bindingResult, final Model model) {
        Category sameCategory = categoryService.getFirstByName(categoryDTO.getCategoryName());

        if (sameCategory != null) {
            bindingResult.addError(new ObjectError("globalError", "Same category already exists."));
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryDTO", categoryDTO);
        } else {
            Category category = new Category();
            category.setName(categoryDTO.getCategoryName());
            categoryService.saveCategory(category);
        }

        model.addAttribute("listCategories", categoryService.getAllCategories());
        return "categories";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable(value = "id") Integer id, Model model) {
        Category category = categoryService.getCategoryById(id);
        List<Article> articlesNotInCategory = articleService.getArticlesNotInCategory(category);

        model.addAttribute("category", category);
        model.addAttribute("articlesNotInCategory", articlesNotInCategory);
        return "category";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable(value = "id") Integer id,
                         @RequestParam(value = "confirm", defaultValue = "false") boolean confirm,
                         RedirectAttributes redir) {
        Category category;
        try {
            category = categoryService.getCategoryById(id);
        } catch (RuntimeException e) {
            return "redirect:/categories";
        }

        if (category.getArticles().isEmpty() || confirm) {
            try {
                categoryService.deleteCategory(id);
            } catch (RuntimeException e) {
                return "redirect:/categories";
            }
        } else {
            String delError = "You cannot delete this category as it has articles bound.";
            redir.addFlashAttribute("delError", delError);
            return "redirect:/categories/" + id;
        }
        redir.addFlashAttribute("listCategories", categoryService.getAllCategories());
        redir.addFlashAttribute("categoryDTO", new CategoryDTO());
        return "redirect:/categories";
    }

    @DeleteMapping("/{idCategory}/articles/{idArticle}")
    public String removeArticle(@PathVariable(value = "idCategory") Integer idCategory,
                                @PathVariable(value = "idArticle") Integer idArticle,
                                RedirectAttributes redir) {
        Category category;
        try {
            category = categoryService.getCategoryById(idCategory);
        } catch (RuntimeException e) {
            String delError = "An error occured when we tried to find the category selected";
            redir.addFlashAttribute("delError", delError);
            return "redirect:/categories/" + idCategory;
        }

        try {
            articleService.removeCategory(idArticle, category);
        } catch (RuntimeException e) {
            String delError = "An error occured when we tried to delete the category from the article selected";
            redir.addFlashAttribute("delError", delError);
            return "redirect:/categories/" + idCategory;
        }

        return "redirect:/categories/" + idCategory;
    }

    @PostMapping("/{idCategory}/articles/{idArticle}")
    public String addArticle(@PathVariable(value = "idCategory") Integer idCategory,
                             @PathVariable(value = "idArticle") Integer idArticle,
                             RedirectAttributes redir) {
        Category category;
        try {
            category = categoryService.getCategoryById(idCategory);
        } catch (RuntimeException e) {
            String delError = "An error occured when we tried to find the category selected";
            redir.addFlashAttribute("delError", delError);
            return "redirect:/categories/" + idCategory;
        }

        try {
            articleService.addCategory(idArticle, category);
        } catch (RuntimeException e) {
            String delError = "An error occured when we tried to add the category from the article selected";
            redir.addFlashAttribute("delError", delError);
            return "redirect:/categories/" + idCategory;
        }

        return "redirect:/categories/" + idCategory;
    }
}