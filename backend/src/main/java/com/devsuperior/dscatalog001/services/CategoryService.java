package com.devsuperior.dscatalog001.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog001.dto.CategoryDTO;
import com.devsuperior.dscatalog001.entities.Category;
import com.devsuperior.dscatalog001.repositories.CategoryRepository;

@Service
public class CategoryService {

	private final CategoryRepository repository;
	CategoryService(CategoryRepository repository) {
		this.repository = repository;
	}
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		List<Category> list = repository.findAll();
		
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
		
	}
}
