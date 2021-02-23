package com.bjpowernode.springboot.blog.dao;

import com.bjpowernode.springboot.blog.entity.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long>, JpaSpecificationExecutor<Comment> {

    List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);
}
