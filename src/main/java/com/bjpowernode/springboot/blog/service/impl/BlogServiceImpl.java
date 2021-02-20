package com.bjpowernode.springboot.blog.service.impl;

import com.bjpowernode.springboot.blog.dao.BlogRepository;
import com.bjpowernode.springboot.blog.entity.Blog;
import com.bjpowernode.springboot.blog.entity.Type;
import com.bjpowernode.springboot.blog.exception.NotFoundException;
import com.bjpowernode.springboot.blog.service.BlogService;
import com.bjpowernode.springboot.blog.utils.MyBeanUtils;
import com.bjpowernode.springboot.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Blog getBlog(Long id) {
        Optional<Blog> byId = blogRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        } else {
            throw new NotFoundException("Blog Not Found");
        }
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (null != blog.getTitle() && !blog.getTitle().equals("")) {
                    predicateList.add(criteriaBuilder.like(root.<String>get("title"), "%" + blog.getTitle() + "%"));
                }
                //if (blog.getType().getId() != null) {
                if (blog.getTypeId() != null) {
                    predicateList.add(criteriaBuilder.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                if (blog.isRecommed()) {
                    predicateList.add(criteriaBuilder.equal(root.<Boolean>get("recommend"), blog.isRecommed()));
                }
                //差不多
                criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
                return null;
            }
        }, pageable);
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null){
            blog.setCreateTime(new Date());
            blog.setViews(0);
        }
        blog.setUpdateTime(new Date());

        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Optional<Blog> blog1 = blogRepository.findById(id);
        if (blog1.isPresent()) {
            BeanUtils.copyProperties(blog, blog1, MyBeanUtils.getNullPropertyNames(blog));
            blog1.get().setUpdateTime(new Date());
            return blogRepository.save(blog1.get());
        } else {
            throw new NotFoundException("Blog Not Found");
        }
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        Optional<Blog> blog2 = blogRepository.findById(id);
        if (blog2.isPresent()) {
            blogRepository.delete(blog2.get());
        } else {
            throw new NotFoundException("Blog Not Found");
        }
    }
}
