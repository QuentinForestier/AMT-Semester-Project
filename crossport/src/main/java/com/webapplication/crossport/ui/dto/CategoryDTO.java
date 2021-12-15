package com.webapplication.crossport.ui.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

/**
 * Implements an object to receive categories creation
 *
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
 */
@Getter
@Setter
public class CategoryDTO {
    @NotBlank(message = "Category name cannot be empty")
    String categoryName;
}