package com.anshuit.writeit.services;

import java.util.List;

import com.anshuit.writeit.entities.Category;

public interface CategoryService {
	Category saveOrUpdateCategory(Category category);
	Category updateCategory(Category category,Integer id);
	void deleteCategory(Integer id);
	Category getCategoryById(Integer id);
	List<Category> getAllCategories();
}
