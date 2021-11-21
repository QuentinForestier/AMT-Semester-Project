package com.webapplication.crossport.models.repository;

import com.webapplication.crossport.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	@Query("select c from Category c where c.name = :name")
	Category findFirstByName(@Param("name") String name);
}
