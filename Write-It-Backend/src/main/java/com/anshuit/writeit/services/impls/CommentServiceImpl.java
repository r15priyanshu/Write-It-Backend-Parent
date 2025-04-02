package com.anshuit.writeit.services.impls;

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
import com.anshuit.writeit.services.CommentService;

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
				() -> new CustomException(HttpStatus.NOT_FOUND, "Username not found with name : " + username));
		Post foundpost = postRepository.findById(postid)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Post not found with id : " + postid));
		comment.setCommentDate(new Date());
		comment.setUser(founduser);
		comment.setPost(foundpost);
		return commentRepository.save(comment);
	}

	@Override
	public void deleteComment(String username, Integer commentid) {
		userRepository.findUserByUsername(username).orElseThrow(
				() -> new CustomException(HttpStatus.NOT_FOUND, "Username not found with name : " + username));
		commentRepository.findById(commentid).orElseThrow(
				() -> new CustomException(HttpStatus.NOT_FOUND, "Comment not found with id : " + commentid));
		commentRepository.deleteById(commentid);
	}

}
