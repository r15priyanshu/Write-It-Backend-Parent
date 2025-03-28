package com.anshuit.writeit.services;

import com.anshuit.writeit.entities.Comment;

public interface CommentService {

	Comment createComment(Comment comment,String username,Integer postid);
	void deleteComment(String username,Integer commentid);
}
