package com.devsuperior.dscatalog001.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.devsuperior.dscatalog001.entities.Category;
import com.devsuperior.dscatalog001.repositories.CategoryRepository;

@Service
public class CategoryService {

	private final CategoryRepository repository;
	CategoryService(CategoryRepository repository) {
		this.repository = repository;
	}
	
	public List<Category> findAll() {
		return repository.findAll();
	}
	
}
