package com.webapplication.crossport.controllers;

import com.webapplication.crossport.models.Article;
import com.webapplication.crossport.models.services.ArticleService;
import com.webapplication.crossport.service.ArticleData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.shaded.org.apache.commons.io.FilenameUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Objects;

@Controller
@RequestMapping(value = {"/articles"})
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    static String[] extensions = new String[]{"jpeg", "jpg", "gif", "png"};

    @GetMapping("/manage")
    public String viewArticles(Model model) {
        model.addAttribute("listArticles", articleService.getAllArticles());
        return "manageArticles";
    }

    @GetMapping("/edit")
    public String editArticle(HttpServletRequest request, Model model, @RequestParam(value = "id", required = false) Integer id) {
        ArticleData ad = new ArticleData();
        if (id != null) {
            Article a = articleService.getArticleById(id);
            ad.setArticleDesc(a.getDescription());
            ad.setArticleName(a.getName());
            ad.setArticleStock(a.isInStock());
            ad.setArticlePrice(a.getNullablePrice());
            ad.setImgPath(a.getImgPath());
        }
        model.addAttribute("id", id);
        model.addAttribute("articleData", ad);
        return "editArticle";
    }

    // Les trois méthodes qui suivent doivent avoir la meme signature

    @PostMapping(value = "/edit", params = "Cancel")
    public String cancelPostArticle(final @ModelAttribute @Valid ArticleData articleData,
                                    final BindingResult bindingResult,
                                    final Model model,
                                    @RequestParam(value = "id", required = false) Integer id,
                                    @RequestParam(value = "image", required = false) MultipartFile multipartFile) {
        return "redirect:manage";
    }

    @PostMapping(value = "/edit", params = "DeleteImage")
    public String deleteImage(final @ModelAttribute @Valid ArticleData articleData,
                              final BindingResult bindingResult,
                              final Model model,
                              @RequestParam(value = "id", required = false) Integer id,
                              @RequestParam(value = "image", required = false) MultipartFile multipartFile) {
        if (id != null) {
            Article article = articleService.getArticleById(id);
            removeFile(id, article.getImgExtension());
            article.setImgExtension(null);
            articleService.modifyArticle(article);
        }

        model.addAttribute("id", id);
        model.addAttribute("articleData", articleData);
        return "editArticle";
    }

    @PostMapping(value = "/edit", params = "Submit")
    public String postArticle(final @ModelAttribute @Valid ArticleData articleData,
                              final BindingResult bindingResult,
                              final Model model,
                              @RequestParam(value = "id", required = false) Integer id,
                              @RequestParam(value = "image", required = false) MultipartFile multipartFile) {

        if (articleData.getArticlePrice() != null && articleData.getArticlePrice() <= 0) {
            bindingResult.addError(
                    new ObjectError("priceError", "The price of the article must be greater than 0"));
        }

        // Is an authorized extension
        if(!multipartFile.isEmpty() && !Arrays.asList(extensions).contains(FilenameUtils.getExtension(multipartFile.getOriginalFilename()))) {
            bindingResult.addError(
                    new ObjectError("priceError", "File extension not supported"));
        }

        Article sameName = articleService.findFirstByName(articleData.getArticleName());
        if (sameName != null && !Objects.equals(sameName.getId(), id)) {
            bindingResult.addError(
                    new ObjectError("nameError", "Two article cannot have the same name"));
        }

        if (bindingResult.hasErrors()) {
            if (id != null) {
                articleData.setImgPath(articleService.getArticleById(id).getImgPath());
            }

            model.addAttribute("id", id);
            model.addAttribute("articleData", articleData);
            return "editArticle";
        }

        Article article;
        if (id == null) {
            article = new Article();
        } else {
            article = articleService.getArticleById(id);
        }

        if (!multipartFile.isEmpty()) {
            article.setImgExtension(getExtension(multipartFile));
        }

        article.setPrice(articleData.getArticlePrice());
        article.setName(articleData.getArticleName());
        article.setDescription(articleData.getArticleDesc());
        article.setInStock(articleData.isArticleStock());
        articleService.modifyArticle(article);

        if (!multipartFile.isEmpty()) {
            if (id == null) {
                id = articleService.findFirstByName(articleData.getArticleName()).getId();
            }

            saveFile(id, multipartFile);
        }

        return "redirect:manage";
    }

    private static void saveFile(Integer id, MultipartFile multipartFile) {
        Path uploadPath = Paths.get(uploadDir);
        String fileName = id.toString() + getExtension(multipartFile);

        try (InputStream inputStream = multipartFile.getInputStream()) {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }

    private static void removeFile(Integer id, String extension) {
        Path uploadPath = Paths.get(uploadDir);
        String fileName = id.toString() + extension;
        Path filePath = uploadPath.resolve(fileName);
        try {
            Files.delete(filePath);
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }

    private static String getExtension(MultipartFile multipartFile) {
        String extension = "";
        String fileName = multipartFile.getOriginalFilename();

        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        if (i > p) {
            extension = multipartFile.getOriginalFilename().substring(i + 1);
            extension = "." + extension;
        }
        return extension;
    }

    public static final String uploadDir = "ProductsImages";
}