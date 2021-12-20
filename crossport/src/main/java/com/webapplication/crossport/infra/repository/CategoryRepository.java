package com.webapplication.crossport.infra.repository;

import com.webapplication.crossport.infra.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	Category findFirstByName(@Param("name") String name);

	List<Category> findByOrderByNameAsc();
}
