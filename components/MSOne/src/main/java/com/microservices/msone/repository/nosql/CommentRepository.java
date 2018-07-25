/*package com.microservices.msone.repository.nosql;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.microservices.msone.model.Comment;

@Repository
public interface CommentRepository extends MongoRepository<Comment, Long> {
	Page<Comment> findByPostId(Long postId, Pageable pageable);
}*/