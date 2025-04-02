package com.anshuit.writeit.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.anshuit.writeit.dto.ApiResponseDto;
import com.anshuit.writeit.dto.CategoryDto;
import com.anshuit.writeit.entities.Category;
import com.anshuit.writeit.enums.ApiResponseEnum;
import com.anshuit.writeit.services.CategoryService;
import com.anshuit.writeit.services.impls.DataTransferServiceImpl;

import jakarta.validation.Valid;

@RestController
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private DataTransferServiceImpl dataTransferService;

	@PostMapping("/categories")
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody Category category) {
		Category createdcategory = categoryService.saveOrUpdateCategory(category);
		CategoryDto createdCategoryDto = dataTransferService.mapCategoryToCategoryDto(createdcategory);
		return new ResponseEntity<>(createdCategoryDto, HttpStatus.CREATED);
	}

	@GetMapping("/categories")
	public ResponseEntity<List<CategoryDto>> getAllCategories() {
		List<CategoryDto> allCategoriesDto = categoryService.getAllCategories().stream()
				.map(category -> dataTransferService.mapCategoryToCategoryDto(category)).collect(Collectors.toList());
		return new ResponseEntity<>(allCategoriesDto, HttpStatus.OK);
	}

	@GetMapping("/categories/{categoryId}")
	public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("cid") int categoryId) {
		Category category = categoryService.getCategoryById(categoryId);
		CategoryDto categoryDto = dataTransferService.mapCategoryToCategoryDto(category);
		return new ResponseEntity<>(categoryDto, HttpStatus.OK);
	}

	@PutMapping("/categories/{categoryId}")
	public ResponseEntity<CategoryDto> updateCategoryById(@PathVariable("categoryId") int categoryId,
			@Valid @RequestBody Category category) {
		Category updatedCategory = categoryService.updateCategory(category, categoryId);
		CategoryDto updatedCategoryDto = dataTransferService.mapCategoryToCategoryDto(updatedCategory);
		return new ResponseEntity<>(updatedCategoryDto, HttpStatus.OK);
	}

	@DeleteMapping("/categories/{categoryId}")
	public ResponseEntity<ApiResponseDto> deleteCategoryById(@PathVariable("categoryId") int categoryId) {
		categoryService.deleteCategory(categoryId);
		ApiResponseDto apiResponseDto = ApiResponseDto
				.generateApiResponse(ApiResponseEnum.CATEGORY_SUCCESSFULLY_DELETED_WITH_ID, categoryId);
		return new ResponseEntity<>(apiResponseDto,
				ApiResponseEnum.CATEGORY_SUCCESSFULLY_DELETED_WITH_ID.getHttpStatus());
	}
}
