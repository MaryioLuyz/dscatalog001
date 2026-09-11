package com.devsuperior.dscatalog001.resources;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.dscatalog001.dto.CategoryDTO;
import com.devsuperior.dscatalog001.services.CategoryService;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

	private final CategoryService service;
	CategoryResource(CategoryService service) {
		this.service = service;
	}
	
	@GetMapping
	public ResponseEntity<List<CategoryDTO>> findAll() {
		List<CategoryDTO> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
}
