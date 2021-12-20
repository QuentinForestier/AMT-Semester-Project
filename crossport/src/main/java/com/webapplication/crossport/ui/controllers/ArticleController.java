package com.webapplication.crossport.ui.controllers;

import com.webapplication.crossport.domain.services.FileService;
import com.webapplication.crossport.infra.models.Article;
import com.webapplication.crossport.domain.services.ArticleService;
import com.webapplication.crossport.ui.dto.ArticleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;

/**
 *  Controller charged to manage articles form admin view
 *
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
 */
@Controller
@RequestMapping(value = {"/articles"})
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    private final FileService fileService = new FileService();

    @GetMapping("")
    public String getAll(Model model) {
        model.addAttribute("listArticles", articleService.getAllArticles());
        return "manageArticles";
    }

    @GetMapping("/new")
    public String newPage(Model model) {
        model.addAttribute("articleDTO", new ArticleDTO());
        return "newArticle";
    }

    @GetMapping("/edit/{id}")
    public String editPage(Model model,
                           @PathVariable(value = "id") Integer id) {
        ArticleDTO articleDTO = new ArticleDTO();
        Article article;
        try {
            article = articleService.getArticleById(id);
        } catch (RuntimeException e) {
            return "manageArticles";
        }

        articleDTO.setArticleDesc(article.getDescription());
        articleDTO.setArticleName(article.getName());
        articleDTO.setArticleStock(article.isInStock());
        articleDTO.setArticlePrice(article.getNullablePrice());
        articleDTO.setImgPath(article.getImgPath());

        model.addAttribute("id", id);
        model.addAttribute("articleDTO", articleDTO);
        return "editArticle";
    }

    @PostMapping(value = "")
    public String add(final @ModelAttribute @Valid ArticleDTO articleDTO,
                      final BindingResult bindingResult,
                      final Model model,
                      @RequestParam(value = "image", required = false) MultipartFile multipartFile) {
        Article article = new Article();

        try {
            articleService.modifyArticle(article, articleDTO, multipartFile, null);
        } catch (RuntimeException e) {
            bindingResult.addError(new ObjectError("Error", e.getMessage()));
            model.addAttribute("articleDTO", articleDTO);
            return "newArticle";
        }

        return "redirect:/articles";
    }

    @PutMapping(value = "", params = "Submit")
    public String edit(final @ModelAttribute @Valid ArticleDTO articleDTO,
                          final BindingResult bindingResult,
                          final Model model,
                          @RequestParam(value = "id", required = true) Integer id,
                          @RequestParam(value = "image", required = false) MultipartFile multipartFile) {
        try {
            Article article = articleService.getArticleById(id);
            articleService.modifyArticle(article, articleDTO, multipartFile, id);
        } catch(RuntimeException e) {
            bindingResult.addError(new ObjectError("Error", e.getMessage()));

            // Remet l'image par défaut
            if (id != null) {
                articleDTO.setImgPath(articleService.getArticleById(id).getImgPath());
            }

            model.addAttribute("id", id);
            model.addAttribute("articleDTO", articleDTO);
            return "editArticle";
        }

        return "redirect:/articles";
    }

    @PutMapping(value = "", params = "DeleteImage")
    public String deleteImage(final @ModelAttribute @Valid ArticleDTO articleDTO,
                              final Model model,
                              @RequestParam(value = "id", required = true) Integer id,
                              @RequestParam(value = "image", required = false) MultipartFile multipartFile) {
        Article article = articleService.getArticleById(id);
        fileService.removeFile(id, article.getImgExtension());
        article.setImgExtension(null);
        articleService.modifyArticleImage(article, articleDTO, multipartFile, id);

        model.addAttribute("id", id);
        model.addAttribute("articleData", articleDTO);
        return "editArticle";
    }
}
