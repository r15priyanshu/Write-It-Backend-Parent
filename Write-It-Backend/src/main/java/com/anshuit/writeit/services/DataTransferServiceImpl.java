package com.anshuit.writeit.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anshuit.writeit.dto.AppUserDto;
import com.anshuit.writeit.dto.CategoryDto;
import com.anshuit.writeit.dto.CommentDto;
import com.anshuit.writeit.dto.PostDto;
import com.anshuit.writeit.entities.AppUser;
import com.anshuit.writeit.entities.Category;
import com.anshuit.writeit.entities.Comment;
import com.anshuit.writeit.entities.Post;

@Service
public class DataTransferServiceImpl {

	@Autowired
	private ModelMapper modelMapper;

	public AppUser mapUserDtoToUser(AppUserDto userDto) {
		return modelMapper.map(userDto, AppUser.class);
	}

	public AppUserDto mapUserToUserDto(AppUser user) {
		return modelMapper.map(user, AppUserDto.class);
	}

	public Category mapCategoryDtoToCategory(CategoryDto categoryDto) {
		return modelMapper.map(categoryDto, Category.class);
	}

	public CategoryDto mapCategoryToCategoryDto(Category category) {
		return modelMapper.map(category, CategoryDto.class);
	}

	public Comment mapCommentDtoToComment(CommentDto commentDto) {
		return modelMapper.map(commentDto, Comment.class);
	}

	public CommentDto mapCommentToCommentDto(Comment comment) {
		return modelMapper.map(comment, CommentDto.class);
	}

	public Post mapPostDtoToPost(PostDto postDto) {
		return modelMapper.map(postDto, Post.class);
	}

	public PostDto mapPostToPostDto(Post post) {
		return modelMapper.map(post, PostDto.class);
	}
}
