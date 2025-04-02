package com.anshuit.writeit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.anshuit.writeit.dto.ApiResponseDto;
import com.anshuit.writeit.dto.CommentDto;
import com.anshuit.writeit.entities.Comment;
import com.anshuit.writeit.enums.ApiResponseEnum;
import com.anshuit.writeit.services.CommentService;
import com.anshuit.writeit.services.impls.DataTransferServiceImpl;

@RestController
public class CommentController {

	@Autowired
	private CommentService commentService;

	@Autowired
	private DataTransferServiceImpl dataTransferService;

	@PostMapping("/users/{username}/posts/{postId}/comments")
	public ResponseEntity<CommentDto> addNewComment(@RequestBody Comment comment,
			@PathVariable("username") String username, @PathVariable("postId") int postId) {
		Comment createdComment = commentService.createComment(comment, username, postId);
		CommentDto commentDto = dataTransferService.mapCommentToCommentDto(createdComment);
		return new ResponseEntity<>(commentDto, HttpStatus.CREATED);
	}

	@DeleteMapping("/users/{username}/posts/{postId}/comments/{commentId}")
	public ResponseEntity<ApiResponseDto> deleteCommentByCommentId(@PathVariable("username") String username,
			@PathVariable("postId") int postId, @PathVariable("commentId") int commentId) {

		commentService.deleteComment(username, commentId);
		ApiResponseDto apiResponseDto = ApiResponseDto
				.generateApiResponse(ApiResponseEnum.COMMENT_SUCCESSFULLY_DELETED_WITH_ID, commentId);
		return new ResponseEntity<ApiResponseDto>(apiResponseDto, HttpStatus.OK);
	}
}
