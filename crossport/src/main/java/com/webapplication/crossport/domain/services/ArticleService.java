package com.webapplication.crossport.domain.services;

import com.webapplication.crossport.infra.models.Article;
import com.webapplication.crossport.infra.models.Category;
import com.webapplication.crossport.infra.repository.ArticleRepository;
import com.webapplication.crossport.infra.repository.CategoryRepository;
import com.webapplication.crossport.ui.dto.ArticleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

/**
 *
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
 */
@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileService fileService;

    public List<Article> getAllArticles() {
        try {
            return articleRepository.findAll();
        } catch (Exception e) {
            return new LinkedList<>();
        }
    }

    public List<Article> getCategoryArticles(Category category) {
        try {
            return articleRepository.findArticlesByCategoriesContaining(category);
        } catch (Exception e) {
            return new LinkedList<>();
        }
    }

    public List<Article> getArticlesNotInCategory(Category category) {
        try {
            return articleRepository.findArticlesByCategoriesNotContaining(category);
        } catch (Exception e) {
            return new LinkedList<>();
        }
    }

    public Article getArticleById(Integer id) {
        Optional<Article> optional = articleRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Article not found for id :: " + id);
        }
    }

    public Article findFirstByName(String name) {
        Article article = articleRepository.findFirstByName(name);
        if (article != null) {
            return article;
        } else {
            throw new RuntimeException("Article not found for name :: " + name);
        }
    }

    public void removeCategory(Integer idArticle, Integer idCategory) {
        Category category = categoryService.getCategoryById(idCategory);
        Article article = getArticleById(idArticle);

        article.removeCategory(category);

        // Important to update BD
        articleRepository.saveAndFlush(article);
        categoryRepository.saveAndFlush(category);
    }

    public void addCategory(Integer idArticle, Integer idCategory) {
        Category category = categoryService.getCategoryById(idCategory);
        Article article = getArticleById(idArticle);

        article.addCategory(category);

        // Important to update BD
        articleRepository.saveAndFlush(article);
        categoryRepository.saveAndFlush(category);
    }

    public void modifyArticle(Article article, ArticleDTO articleDTO, MultipartFile multipartFile, Integer id) {
        validateArticleOrThrow(articleDTO, id);
        validateFileOrThrow(multipartFile);

        article.setPrice(articleDTO.getArticlePrice());
        article.setName(articleDTO.getArticleName());
        article.setDescription(articleDTO.getArticleDesc());
        article.setInStock(articleDTO.isArticleStock());

        if(article.getImgPath() != null) {
            fileService.removeFile(article.getId(), article.getImgExtension());
        }

        setArticleImageExtension(article, multipartFile);

        articleRepository.save(article);

        saveArticleImage(articleDTO, multipartFile, id);
    }

    public void modifyArticleImage(Article article, ArticleDTO articleDTO, MultipartFile multipartFile, Integer id) {
        validateFileOrThrow(multipartFile);
        setArticleImageExtension(article, multipartFile);
        saveArticleImage(articleDTO, multipartFile, id);
        articleRepository.save(article);
    }

    private void setArticleImageExtension(Article article, MultipartFile multipartFile) {
        if (!multipartFile.isEmpty()) {
            article.setImgExtension(fileService.getExtension(multipartFile));
        }
    }

    private void saveArticleImage(ArticleDTO articleDTO, MultipartFile multipartFile, Integer id) {
        if (!multipartFile.isEmpty()) {
            if (id == null) {
                id = this.findFirstByName(articleDTO.getArticleName()).getId();
            }
            fileService.saveFile(id, multipartFile);
        }
    }

    private void validateArticleOrThrow(ArticleDTO articleDTO, Integer id) {
        if (articleDTO.getArticlePrice() != null && !isAValidPrice(articleDTO.getArticlePrice())) {
            throw new RuntimeException("The price of the article must be greater than 0");
        }

        if (!isAFreeName(articleDTO.getArticleName(), id)) {
            throw new RuntimeException("Two article cannot have the same name");
        }
    }

    private void validateFileOrThrow(MultipartFile multipartFile) {
        if(!multipartFile.isEmpty() && !fileService.isAnAuthorizedExtension(multipartFile)) {
            throw new RuntimeException("File extension not supported");
        }
    }

    private boolean isAFreeName(String name, Integer id) {
        Article sameName;
        try {
            sameName = this.findFirstByName(name);
        } catch (RuntimeException e) {
            return true;
        }

        return id != null && Objects.equals(sameName.getId(), id);
    }

    private boolean isAValidPrice(Double price) {
        return price > 0 && price < 100000;
    }
}