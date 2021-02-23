package com.bjpowernode.springboot.blog.service.impl;

import com.bjpowernode.springboot.blog.dao.AvatarRepository;
import com.bjpowernode.springboot.blog.dao.BlogRepository;
import com.bjpowernode.springboot.blog.entity.Avatar;
import com.bjpowernode.springboot.blog.entity.Blog;
import com.bjpowernode.springboot.blog.entity.Type;
import com.bjpowernode.springboot.blog.exception.NotFoundException;
import com.bjpowernode.springboot.blog.service.BlogService;
import com.bjpowernode.springboot.blog.utils.MarkdownUtils;
import com.bjpowernode.springboot.blog.utils.MyBeanUtils;
import com.bjpowernode.springboot.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private AvatarRepository avatarRepository;

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

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        Page<Blog> byQuery = blogRepository.findByQuery("%" + query + "%", pageable);
        return byQuery;
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, Long tagId) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Join join = root.join("tags");
                return criteriaBuilder.equal(join.get("id"),tagId);
            }
        },pageable);
    }

    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Optional<Blog> byId = blogRepository.findById(id);
        if (!byId.isPresent()){
            throw new NotFoundException("Blog Not Found");
        }else{
            Blog b = new Blog();
            BeanUtils.copyProperties(byId.get(),b);
            String content = b.getContent();
            String value = MarkdownUtils.markDownToHTMLExtensions(content);
            b.setContent(value);
            b.setViews(blogRepository.updateView(id));
            return b;
        }
    }

    @Override
    public List<Blog> listRecommendationsBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"updateTime");
//        Pageable pageable = new PageRequest(0,size,sort);
        Pageable pageable = PageRequest.of(0, size, sort);
        return blogRepository.findTop(pageable);
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
        Optional<Blog> oldBlog = blogRepository.findById(id);
        System.out.println(oldBlog.get());
        System.out.println("NEW blog ======== "+blog);
        if (oldBlog.isPresent()) {
            BeanUtils.copyProperties(blog, oldBlog.get(), MyBeanUtils.getNullPropertyNames(blog));
            oldBlog.get().setUpdateTime(new Date());
            System.out.println("Old blog ======== "+oldBlog);
            return blogRepository.save(oldBlog.get());
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

    @Transactional
    @Override
    public void addAvatar(String path) {
        Avatar avatar = new Avatar();
        avatar.setAvatarPath(path);
        avatarRepository.save(avatar);
    }

    @Override
    public Page<Blog> listRecommendationsBlog(Pageable pageable) {
        Sort sort = Sort.by(Sort.Direction.DESC,"updateTime");
        Pageable pageable1 = PageRequest.of(0, 2, sort);
        System.out.println(pageable1.getPageSize());
        return blogRepository.findRecommeded(pageable1);
    }

    @Override
    public Map<String, List<Blog>> archieveBlog() {
        Map<String, List<Blog>> map = new HashMap<>();
        List<String> years = blogRepository.findGroupYears();
        for (String year : years) {
            map.put(year,blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }
}
