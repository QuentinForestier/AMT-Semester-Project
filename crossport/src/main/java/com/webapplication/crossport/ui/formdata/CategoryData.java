package com.webapplication.crossport.ui.formdata;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Implements a serializable object to receive categories creation
 *
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
 */
@Getter
@Setter
public class CategoryData implements Serializable {
    @NotBlank(message = "Category name cannot be empty")
    String categoryName;
}