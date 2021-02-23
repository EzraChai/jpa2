package com.bjpowernode.springboot.blog.service;

import com.bjpowernode.springboot.blog.entity.Blog;
import com.bjpowernode.springboot.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {

    Blog getBlog(Long id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlog(String query,Pageable pageable);

    Page<Blog> listBlog(Pageable pageable,Long tagId);

    Blog getAndConvert(Long id);

    List<Blog> listRecommendationsBlogTop(Integer size);

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id,Blog blog);

    void deleteBlog(Long id);

    void addAvatar(String path);

    Page<Blog> listRecommendationsBlog(Pageable pageable);

    Map<String, List<Blog>> archieveBlog();

    Long countBlog();
}
