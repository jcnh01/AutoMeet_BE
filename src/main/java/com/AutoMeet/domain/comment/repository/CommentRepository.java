package com.AutoMeet.domain.comment.repository;

import com.AutoMeet.domain.comment.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, String> {
}