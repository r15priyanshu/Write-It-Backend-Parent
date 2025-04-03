package com.anshuit.writeit.services;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.anshuit.writeit.dto.PostResponseDto;
import com.anshuit.writeit.entities.Post;

public interface PostService {
	Post saveOrUpdatePost(Post post);

	Post createPostAndSaveImageInDB(Post post, int userId, int categoryId, MultipartFile file);

	Post addImageToPost(MultipartFile file, int userId, int postId);

	Optional<Post> getPostByIdOptional(int postId);

	Post getPostById(int postId);

	PostResponseDto getAllPosts(int pageNumber, int pageSize, boolean mostRecentFirst);

	PostResponseDto getAllPostsByCategoryId(int categoryId, int pageNumber, int pageSize, boolean mostRecentFirst);

	List<Post> getAllPostsByUserId(int userId, boolean mostRecentFirst);

	Post updatePostById(Post newPostData, int postId, int userId);

	void deletePostById(Integer id);
}
