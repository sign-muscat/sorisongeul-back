package com.sorisonsoon.comment.domain.repository;

import com.sorisonsoon.comment.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
