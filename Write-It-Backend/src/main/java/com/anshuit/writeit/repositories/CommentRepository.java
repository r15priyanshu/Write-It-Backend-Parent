package com.anshuit.writeit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anshuit.writeit.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
