package com.anshuit.writeit.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.anshuit.writeit.entities.AppUser;
import com.anshuit.writeit.entities.Comment;
import com.anshuit.writeit.entities.Post;
import com.anshuit.writeit.exceptions.CustomException;
import com.anshuit.writeit.repositories.CommentRepository;
import com.anshuit.writeit.repositories.PostRepository;
import com.anshuit.writeit.repositories.UserRepository;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	PostRepository postRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	CommentRepository commentRepository;

	@Override
	public Comment createComment(Comment comment, String username, Integer postid) {
		AppUser founduser = userRepository.findUserByUsername(username).orElseThrow(
				() -> new CustomException("Username not found with name : " + username, HttpStatus.NOT_FOUND));
		Post foundpost = postRepository.findById(postid)
				.orElseThrow(() -> new CustomException("Post not found with id : " + postid, HttpStatus.NOT_FOUND));
		comment.setCommentDate(new Date());
		comment.setUser(founduser);
		comment.setPost(foundpost);
		return commentRepository.save(comment);
	}

	@Override
	public void deleteComment(String username, Integer commentid) {
		userRepository.findUserByUsername(username).orElseThrow(
				() -> new CustomException("Username not found with name : " + username, HttpStatus.NOT_FOUND));
		commentRepository.findById(commentid).orElseThrow(
				() -> new CustomException("Comment not found with id : " + commentid, HttpStatus.NOT_FOUND));
		commentRepository.deleteById(commentid);
	}

}
