package com.anshuit.writeit.services;

import java.util.List;
import java.util.Optional;

import com.anshuit.writeit.entities.Category;

public interface CategoryService {
	Category saveOrUpdateCategory(Category category);

	Category createCategory(Category category);

	Optional<Category> getCategoryByIdOptional(int categoryId);

	Category getCategoryById(int categoryId);

	List<Category> getAllCategories();

	Category updateCategoryById(Category category, int categoryId);

	void deleteCategory(int categoryId);
}
