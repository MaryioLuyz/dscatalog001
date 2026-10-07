package com.devsuperior.dscatalog001.services;

import static org.mockito.Mockito.times;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog001.dto.ProductDTO;
import com.devsuperior.dscatalog001.entities.Category;
import com.devsuperior.dscatalog001.entities.Product;
import com.devsuperior.dscatalog001.repositories.CategoryRepository;
import com.devsuperior.dscatalog001.repositories.ProductRepository;
import com.devsuperior.dscatalog001.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog001.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog001.tests.Factory;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private Product product;
	private Category category;
	ProductDTO productDTO;
	private  PageImpl<Product> page;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		product = Factory.createProduct();
		category = Factory.createCategory();
		productDTO = Factory.createProductDTO();
		page = new PageImpl<>(List.of(product));
		
		Mockito.lenient().when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		
		Mockito.lenient().when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		
		Mockito.lenient().when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.lenient().when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		Mockito.lenient().when(repository.getReferenceById(existingId)).thenReturn(product);
		Mockito.lenient().when(repository.getReferenceById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		Mockito.lenient().when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
		Mockito.lenient().when(categoryRepository.getReferenceById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		Mockito.lenient().doNothing().when(repository).deleteById(existingId);
		Mockito.lenient().doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
		
		Mockito.lenient().when(repository.existsById(existingId)).thenReturn(true);
		Mockito.lenient().when(repository.existsById(nonExistingId)).thenReturn(false);
		Mockito.lenient().when(repository.existsById(dependentId)).thenReturn(true);
		
		
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class,() -> {
			service.update(nonExistingId, productDTO);
		});
	}
	
	@Test
	public void updateIdShouldReturnProductDTOWhenIdExists() {
		
		ProductDTO result = service.update(existingId, productDTO);
		
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class,() -> {
			service.findById(nonExistingId);
		});
	}
	
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {
		
		ProductDTO result = service.findById(existingId);
		
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void findAllPagedShouldReturnPage() {
		
		Pageable pageable = PageRequest.of(0, 10);
		
		Page<ProductDTO> result = service.findAllPaged(pageable);
		
		Assertions.assertNotNull(result);
		Mockito.verify(repository).findAll(pageable);
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
		
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentId);
		});
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
		
		Mockito.verify(repository, times(1)).deleteById(existingId);
	}
}
