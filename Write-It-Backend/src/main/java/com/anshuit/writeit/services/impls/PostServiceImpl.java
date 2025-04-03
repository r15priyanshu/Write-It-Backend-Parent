package com.anshuit.writeit.services.impls;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.anshuit.writeit.constants.GlobalConstants;
import com.anshuit.writeit.dto.PostDto;
import com.anshuit.writeit.dto.PostResponseDto;
import com.anshuit.writeit.entities.AppUser;
import com.anshuit.writeit.entities.Category;
import com.anshuit.writeit.entities.Post;
import com.anshuit.writeit.exceptions.CustomException;
import com.anshuit.writeit.exceptions.enums.ExceptionDetailsEnum;
import com.anshuit.writeit.repositories.PostRepository;
import com.anshuit.writeit.services.CategoryService;
import com.anshuit.writeit.services.FileService;
import com.anshuit.writeit.services.PostService;
import com.anshuit.writeit.services.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PostServiceImpl implements PostService {
	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private FileService fileService;

	@Autowired
	private DataTransferServiceImpl dataTransferService;

	@Override
	public Post saveOrUpdatePost(Post post) {
		return postRepository.save(post);
	}

	@Override
	public Post createPostAndSaveImageInDB(Post post, int userId, int categoryId, MultipartFile file) {
		AppUser foundUser = userService.getUserById(userId);
		Category foundCategory = categoryService.getCategoryById(categoryId);
		post.setCreatedDate(new Date());
		post.setCategory(foundCategory);
		post.setUser(foundUser);

		if (file != null && fileService.isImageWithValidExtension(file)) {
			try {
				byte[] imageData = file.getBytes();
				post.setImageData(imageData);
				post.setImage(GlobalConstants.POST_IMAGE_UPLOADED);
			} catch (Exception e) {
				log.error("Error In Uploading Image Along With Post!!");
				throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR,
						ExceptionDetailsEnum.ERROR_IN_UPLOADING_IMAGE_WITH_POST);
			}
		}
		Post createdPost = this.saveOrUpdatePost(post);
		return createdPost;
	}

	@Override
	public Post addImageToPost(MultipartFile file, int userId, int postId) {
		userService.getUserById(userId);
		Post foundPost = this.getPostById(postId);

		if (file != null && fileService.isImageWithValidExtension(file)) {
			try {
				byte[] imageData = file.getBytes();
				foundPost.setImageData(imageData);
				foundPost.setImage(GlobalConstants.POST_IMAGE_UPLOADED);
			} catch (Exception e) {
				log.error("Error In Adding Image To A Post!!");
				throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR,
						ExceptionDetailsEnum.ERROR_IN_UPLOADING_IMAGE_TO_ALREADY_UPLOADED_POST, postId);
			}
		}
		return this.saveOrUpdatePost(foundPost);
	}

	@Override
	public Optional<Post> getPostByIdOptional(int postId) {
		return postRepository.findById(postId);
	}

	@Override
	public Post getPostById(int postId) {
		Post foundPost = getPostByIdOptional(postId).orElseThrow(
				() -> new CustomException(HttpStatus.NOT_FOUND, ExceptionDetailsEnum.POST_NOT_FOUND_WITH_ID, postId));
		return foundPost;
	}

	@Override
	public PostResponseDto getAllPostsByCategoryId(int categoryId, int pageNumber, int pageSize,
			boolean mostRecentFirst) {
		Sort sort = Sort.by(mostRecentFirst ? Direction.DESC : Direction.ASC, "createdDate");
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Post> pageInfo = null;

		if (categoryId == 0) {
			pageInfo = postRepository.findAll(pageable);
		} else {
			Category foundCategory = categoryService.getCategoryById(categoryId);
			pageInfo = postRepository.findPostByCategory(foundCategory, pageable);
		}
		List<Post> posts = pageInfo.getContent();
		List<PostDto> postsDtos = posts.stream().map(post -> dataTransferService.mapPostToPostDto(post))
				.collect(Collectors.toList());

		PostResponseDto postResponseDto = new PostResponseDto();
		postResponseDto.setPosts(postsDtos);
		postResponseDto.setCurrentpage(pageInfo.getNumber());
		postResponseDto.setIslastpage(pageInfo.isLast());
		postResponseDto.setTotalpage(pageInfo.getTotalPages());
		postResponseDto.setTotalposts(pageInfo.getTotalElements());
		return postResponseDto;
	}

	@Override
	public PostResponseDto getAllPosts(int pageNumber, int pageSize, boolean mostRecentFirst) {
		Sort sort = Sort.by(mostRecentFirst ? Direction.DESC : Direction.ASC, "createdDate");
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Post> pageInfo = postRepository.findAll(pageable);
		List<Post> posts = pageInfo.getContent();
		List<PostDto> postsDtos = posts.stream().map(post -> dataTransferService.mapPostToPostDto(post))
				.collect(Collectors.toList());

		PostResponseDto postResponseDto = new PostResponseDto();
		postResponseDto.setPosts(postsDtos);
		postResponseDto.setCurrentpage(pageInfo.getNumber());
		postResponseDto.setIslastpage(pageInfo.isLast());
		postResponseDto.setTotalpage(pageInfo.getTotalPages());
		postResponseDto.setTotalposts(pageInfo.getTotalElements());
		return postResponseDto;
	}

	@Override
	public List<Post> getAllPostsByUserId(int userId, boolean mostRecentFirst) {
		Sort sort = Sort.by(mostRecentFirst ? Direction.DESC : Direction.ASC, "createdDate");
		AppUser foundUser = userService.getUserById(userId);
		return postRepository.findPostByUser(foundUser, sort);
	}

	@Override
	public Post updatePostById(Post newPostData, int postId, int userId) {
		userService.getUserById(userId);
		Post foundPost = this.getPostById(postId);
		foundPost.setTitle(newPostData.getTitle() == null ? foundPost.getTitle() : newPostData.getTitle());
		foundPost.setContent(newPostData.getContent() == null ? foundPost.getContent() : newPostData.getContent());
		return this.saveOrUpdatePost(foundPost);
	}

	@Override
	public void deletePostById(Integer id) {
		postRepository.findById(id)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Post not found with id :" + id));
		postRepository.deleteById(id);
	}

}
