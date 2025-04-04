package com.anshuit.writeit.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.anshuit.writeit.constants.GlobalConstants;
import com.anshuit.writeit.dto.ApiResponseDto;
import com.anshuit.writeit.dto.PostDto;
import com.anshuit.writeit.dto.PostResponseDto;
import com.anshuit.writeit.entities.Post;
import com.anshuit.writeit.enums.ApiResponseEnum;
import com.anshuit.writeit.exceptions.CustomException;
import com.anshuit.writeit.exceptions.enums.ExceptionDetailsEnum;
import com.anshuit.writeit.services.PostService;
import com.anshuit.writeit.services.impls.DataTransferServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class PostController {

	@Autowired
	private PostService postService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private DataTransferServiceImpl dataTransferService;

	// CREATE NEW POST [ USING FORM DATA I.E, INPUTS + FILE ]
	@PostMapping("/users/{userId}/posts/{categoryId}")
	public ResponseEntity<PostDto> createNewPostWithFormData(@RequestParam("post") String post,
			@RequestParam(name = "image", required = false) MultipartFile file, @PathVariable("userId") int userId,
			@PathVariable("categoryId") int categoryId) {

		Post createdPost = null;
		try {
			Post postData = objectMapper.readValue(post, Post.class);
			createdPost = postService.createPostAndSaveImageInDB(postData, userId, categoryId, file);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		PostDto postDto = dataTransferService.mapPostToPostDto(createdPost);
		return new ResponseEntity<PostDto>(postDto, HttpStatus.CREATED);
	}

	// ADD IMAGE TO A POST
	@PostMapping("/users/{userId}/posts/{postId}/image")
	public ResponseEntity<ApiResponseDto> addImageToPost(@RequestParam("image") MultipartFile image,
			@PathVariable("userId") int userId, @PathVariable("postId") int postId) {

		postService.addImageToPost(image, userId, postId);
		ApiResponseDto apiResponse = ApiResponseDto
				.generateApiResponse(ApiResponseEnum.IMAGE_SUCCESSFULLY_ADDED_TO_POST_WITH_ID, postId);
		return new ResponseEntity<ApiResponseDto>(apiResponse, HttpStatus.OK);
	}

	// SERVE POST IMAGE
	@GetMapping(value = "/images/servePostImage/{postId}")
	public ResponseEntity<byte[]> servePostImage(@PathVariable("postId") int postId) {
		Post foundPost = postService.getPostById(postId);
		if (foundPost.getImage().equals(GlobalConstants.DEFAULT_POST_IMAGE_NAME)) {
			throw new CustomException(HttpStatus.OK, ExceptionDetailsEnum.DEFAULT_POST_IMAGE_SET);
		}
		// Detect MIME type of image data
		String contentType = new Tika().detect(foundPost.getImageData());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(contentType));
		return new ResponseEntity<>(foundPost.getImageData(), headers, HttpStatus.OK);
	}

	// GET ALL POSTS OF USER BY USERID
	@GetMapping("/users/{userId}/posts")
	public ResponseEntity<List<PostDto>> getPostByUsername(@PathVariable("userId") int userId,
			@RequestParam(name = "mostRecentFirst", defaultValue = "true", required = false) boolean mostRecentFirst) {
		List<PostDto> allPostsByUser = postService.getAllPostsByUserId(userId, mostRecentFirst).stream()
				.map(post -> dataTransferService.mapPostToPostDto(post)).collect(Collectors.toList());

		return new ResponseEntity<List<PostDto>>(allPostsByUser, HttpStatus.OK);
	}

	// GET SINGLE POST OF USER BY POSTID
	@GetMapping("/users/{userId}/posts/{postId}")
	public ResponseEntity<PostDto> getPostOfUserByPostId(@PathVariable("userId") int userId,
			@PathVariable("postId") int postId) {
		Post post = postService.getPostById(postId);
		PostDto postDto = dataTransferService.mapPostToPostDto(post);
		return new ResponseEntity<PostDto>(postDto, HttpStatus.OK);
	}

	// DELETE SINGLE POST OF USER BY PID
	@DeleteMapping("/users/{username}/posts/{postId}")
	public ResponseEntity<ApiResponseDto> deletePostOfUserByPostId(@PathVariable("username") String username,
			@PathVariable("postId") int postId) {
		postService.deletePostById(postId);
		ApiResponseDto apiResponseDto = ApiResponseDto
				.generateApiResponse(ApiResponseEnum.POST_SUCCESSFULLY_DELETED_WITH_ID, postId);
		return new ResponseEntity<ApiResponseDto>(apiResponseDto,
				ApiResponseEnum.POST_SUCCESSFULLY_DELETED_WITH_ID.getHttpStatus());
	}

	// UPDATE SINGLE POST OF USER BY PID
	@PutMapping("/users/{userId}/posts/{postId}")
	public ResponseEntity<PostDto> updatePostOfUserByPostId(@PathVariable("userId") int userId,
			@PathVariable("postId") int postId, @RequestBody PostDto newPostDataDto) {
		Post newPostData = dataTransferService.mapPostDtoToPost(newPostDataDto);
		Post updatedPost = postService.updatePostById(newPostData, postId, userId);
		PostDto updatedPostDto = dataTransferService.mapPostToPostDto(updatedPost);
		return new ResponseEntity<PostDto>(updatedPostDto, HttpStatus.OK);
	}

	// GET POST BY POSTID
	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostDto> getPostByPostId(@PathVariable("postId") int postId) {
		Post post = postService.getPostById(postId);
		PostDto postDto = dataTransferService.mapPostToPostDto(post);
		return new ResponseEntity<PostDto>(postDto, HttpStatus.OK);
	}

	// GET ALL POST BY CATEGORY-ID
	@GetMapping("/posts/category/{categoryId}")
	public ResponseEntity<PostResponseDto> getAllPostsByCategory(@PathVariable("categoryId") int categoryId,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
			@RequestParam(value = "mostRecentFirst", defaultValue = "true", required = false) boolean mostRecentFirst) {
		PostResponseDto allPostsByCategory = postService.getAllPostsByCategoryId(categoryId, pageNumber, pageSize,
				mostRecentFirst);
		return new ResponseEntity<PostResponseDto>(allPostsByCategory, HttpStatus.OK);
	}

	// GET ALL POSTS
	@GetMapping("/posts")
	public ResponseEntity<PostResponseDto> getAllPosts(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
			@RequestParam(value = "mostRecentFirst", defaultValue = "true", required = false) boolean mostRecentFirst) {
		PostResponseDto allPosts = postService.getAllPosts(pageNumber, pageSize, mostRecentFirst);
		return new ResponseEntity<>(allPosts, HttpStatus.OK);
	}
}
