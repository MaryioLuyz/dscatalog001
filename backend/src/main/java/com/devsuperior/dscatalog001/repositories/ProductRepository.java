package com.devsuperior.dscatalog001.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dscatalog001.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
