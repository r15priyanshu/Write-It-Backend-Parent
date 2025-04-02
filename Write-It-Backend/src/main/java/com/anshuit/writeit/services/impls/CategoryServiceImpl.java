package com.anshuit.writeit.services.impls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.anshuit.writeit.entities.Category;
import com.anshuit.writeit.exceptions.CustomException;
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
	public Category updateCategory(Category category, Integer id) {
		Category foundcategory = categoryRepository.findById(id)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Category not found with id :" + id));
		foundcategory.setCategoryName(category.getCategoryName());
		foundcategory.setCategoryDescription(category.getCategoryDescription());
		return categoryRepository.save(foundcategory);
	}

	@Override
	public void deleteCategory(Integer id) {
		categoryRepository.findById(id)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Category not found with id :" + id));
		categoryRepository.deleteById(id);
	}

	@Override
	public Category getCategoryById(Integer id) {
		Category foundcategory = categoryRepository.findById(id)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Category not found with id :" + id));
		return foundcategory;
	}

	@Override
	public List<Category> getAllCategories() {
		List<Category> allcategories = categoryRepository.findAll();
		if (allcategories.size() == 0)
			throw new CustomException(HttpStatus.NOT_FOUND, "No Category found");
		return allcategories;
	}

}
