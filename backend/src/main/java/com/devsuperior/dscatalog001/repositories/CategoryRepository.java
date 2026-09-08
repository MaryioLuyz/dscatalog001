package com.devsuperior.dscatalog001.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.devsuperior.dscatalog001.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
