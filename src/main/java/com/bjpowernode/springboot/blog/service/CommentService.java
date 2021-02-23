package com.bjpowernode.springboot.blog.service;

import com.bjpowernode.springboot.blog.entity.Comment;

import java.util.List;

public interface CommentService{
    List<Comment> listCommentsByBlogId(Long blogId);
    Comment saveComment(Comment comment);
    String getRandomAvatar();
}
