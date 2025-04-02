package com.anshuit.writeit.services.impls;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
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
import com.anshuit.writeit.repositories.CategoryRepository;
import com.anshuit.writeit.repositories.PostRepository;
import com.anshuit.writeit.repositories.UserRepository;
import com.anshuit.writeit.services.FileService;
import com.anshuit.writeit.services.PostService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PostServiceImpl implements PostService {
	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private FileService fileService;

	@Override
	public Post createPostAndSaveImageInDB(Post post, String username, String categoryname, MultipartFile file) {
		AppUser founduser = userRepository.findUserByUsername(username.toLowerCase())
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,
						"User Not Found with username : " + username.toLowerCase()));

		Category foundcategory = categoryRepository.findCategoryByCategoryName(categoryname).orElseThrow(
				() -> new CustomException(HttpStatus.NOT_FOUND, "Category Not Found with name : " + categoryname));

		post.setCreatedDate(new Date());
		post.setCategory(foundcategory);
		post.setUser(founduser);

		if (file != null && fileService.isImageWithValidExtension(file)) {
			try {
				byte[] imageData = file.getBytes();
				post.setImageData(imageData);
				post.setImage(GlobalConstants.POST_IMAGE_UPLOADED);
			} catch (Exception e) {
				log.error("Error In Uploading Image along with Post!!");
				throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR,
						"Error In Uploading Image along with Post!!");
			}
		}
		return postRepository.save(post);
	}

	@Override
	public Post getPostById(Integer id) {
		return postRepository.findById(id)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Post not found with id :" + id));
	}

	@Override
	public Post addImageToPost(MultipartFile file, String username, Integer postid) {
		userRepository.findUserByUsername(username.toLowerCase())
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,
						"User Not Found with username : " + username.toLowerCase()));

		Post foundPost = postRepository.findById(postid)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Post not found with id :" + postid));

		if (file != null && fileService.isImageWithValidExtension(file)) {
			try {
				byte[] imageData = file.getBytes();
				foundPost.setImageData(imageData);
				foundPost.setImage(GlobalConstants.POST_IMAGE_UPLOADED);
			} catch (Exception e) {
				log.error("Error In Adding Image To A Post!!");
				throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error In Adding Image To A Post!!");
			}
		}
		return postRepository.save(foundPost);
	}

	@Override
	public Post updatePostById(Post newpostdata, Integer postid, String username) {
		userRepository.findUserByUsername(username.toLowerCase())
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,
						"User Not Found with username : " + username.toLowerCase()));

		Post foundPost = postRepository.findById(postid)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Post not found with id :" + postid));

		foundPost.setTitle(newpostdata.getTitle() == null ? foundPost.getTitle() : newpostdata.getTitle());
		foundPost.setContent(newpostdata.getContent() == null ? foundPost.getContent() : newpostdata.getContent());
		return postRepository.save(foundPost);
	}

	@Override
	public void deletePostById(Integer id) {
		postRepository.findById(id)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Post not found with id :" + id));
		postRepository.deleteById(id);
	}

	@Override
	public PostResponseDto getAllPostsByCategory(String category, Integer pagenumber, Integer pagesize,
			boolean mostrecentfirst) {
		Sort sort = Sort.by(mostrecentfirst ? Direction.DESC : Direction.ASC, "date");
		Pageable pageable = PageRequest.of(pagenumber, pagesize, sort);
		Page<Post> pageinfo = null;

		if (category.equals("All")) {
			pageinfo = postRepository.findAll(pageable);
		} else {
			Category foundcategory = categoryRepository.findCategoryByCategoryName(category).orElseThrow(
					() -> new CustomException(HttpStatus.NOT_FOUND, "Category not found with name : " + category));

			pageinfo = postRepository.findPostByCategory(foundcategory, pageable);
		}
		List<Post> posts = pageinfo.getContent();
		List<PostDto> postsdtos = posts.stream().map(post -> modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		PostResponseDto postResponseDto = new PostResponseDto();
		postResponseDto.setPosts(postsdtos);
		postResponseDto.setCurrentpage(pageinfo.getNumber());
		postResponseDto.setIslastpage(pageinfo.isLast());
		postResponseDto.setTotalpage(pageinfo.getTotalPages());
		postResponseDto.setTotalposts(pageinfo.getTotalElements());
		return postResponseDto;
	}

	@Override
	public PostResponseDto getAllPosts(Integer pagenumber, Integer pagesize, boolean mostrecentfirst) {
		Sort sort = Sort.by(mostrecentfirst ? Direction.DESC : Direction.ASC, "date");
		Pageable pageable = PageRequest.of(pagenumber, pagesize, sort);
		Page<Post> pageinfo = postRepository.findAll(pageable);
		List<Post> posts = pageinfo.getContent();
		List<PostDto> postsdtos = posts.stream().map(post -> modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		PostResponseDto postResponseDto = new PostResponseDto();
		postResponseDto.setPosts(postsdtos);
		postResponseDto.setCurrentpage(pageinfo.getNumber());
		postResponseDto.setIslastpage(pageinfo.isLast());
		postResponseDto.setTotalpage(pageinfo.getTotalPages());
		postResponseDto.setTotalposts(pageinfo.getTotalElements());
		return postResponseDto;
	}

	@Override
	public List<Post> getAllPostsByUser(String username, boolean mostrecentfirst) {
		Sort sort = Sort.by(mostrecentfirst ? Direction.DESC : Direction.ASC, "date");
		AppUser founduser = userRepository.findUserByUsername(username)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Username not found in DB :" + username));
		return postRepository.findPostByUser(founduser, sort);
	}
}
