package com.anshuit.writeit.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.anshuit.writeit.entities.AppUser;
import com.anshuit.writeit.entities.Category;
import com.anshuit.writeit.entities.Post;



public interface PostRepository extends JpaRepository<Post,Integer> {
	
	List<Post> findPostByUser(AppUser username);
	List<Post> findPostByUser(AppUser username,Sort sort);
	List<Post> findPostByCategory(Category category);
	Page<Post> findPostByCategory(Category category,Pageable pageable);

}
