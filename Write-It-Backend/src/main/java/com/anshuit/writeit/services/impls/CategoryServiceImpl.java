package com.anshuit.writeit.services.impls;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.anshuit.writeit.entities.Category;
import com.anshuit.writeit.exceptions.CustomException;
import com.anshuit.writeit.exceptions.enums.ExceptionDetailsEnum;
import com.anshuit.writeit.repositories.CategoryRepository;
import com.anshuit.writeit.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Category saveOrUpdateCategory(Category category) {
		return categoryRepository.save(category);
	}

	@Override
	public Category createCategory(Category category) {
		return this.saveOrUpdateCategory(category);
	}

	@Override
	public Optional<Category> getCategoryByIdOptional(int categoryId) {
		return categoryRepository.findById(categoryId);
	}

	@Override
	public Category getCategoryById(int categoryId) {
		Category foundCategory = this.getCategoryByIdOptional(categoryId)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,
						ExceptionDetailsEnum.CATEGORY_NOT_FOUND_WITH_ID, categoryId));
		return foundCategory;
	}

	@Override
	public List<Category> getAllCategories() {
		List<Category> allCategories = categoryRepository.findAll();
		return allCategories;
	}

	@Override
	public Category updateCategoryById(Category category, int categoryId) {
		Category foundCategory = this.getCategoryById(categoryId);
		foundCategory.setCategoryName(category.getCategoryName());
		foundCategory.setCategoryDescription(category.getCategoryDescription());
		return this.saveOrUpdateCategory(foundCategory);
	}

	@Override
	public void deleteCategory(int categoryId) {
		this.getCategoryById(categoryId);
		categoryRepository.deleteById(categoryId);
	}
}
