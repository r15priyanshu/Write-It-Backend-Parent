package com.anshuit.writeit.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anshuit.writeit.entities.Category;

public interface CategoryRepository extends JpaRepository<Category,Integer> {

	Optional<Category> findCategoryByCategoryName(String categoryName);
}
