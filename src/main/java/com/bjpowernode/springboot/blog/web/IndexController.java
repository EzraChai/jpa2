package com.bjpowernode.springboot.blog.web;

import com.bjpowernode.springboot.blog.entity.Blog;
import com.bjpowernode.springboot.blog.service.BlogService;
import com.bjpowernode.springboot.blog.service.TagService;
import com.bjpowernode.springboot.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String index(@PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, Model model){
        model.addAttribute("page",blogService.listBlog(pageable));
        model.addAttribute("types",typeService.listTypeTop(6));
        model.addAttribute("tags",tagService.listTagsTop(10));
        model.addAttribute("recommendBlogs",blogService.listRecommendationsBlogTop(8));
        return "index";
    }

    @PostMapping("/search")
    public String search(@PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, @RequestParam String query, Model model){
        System.out.println(query);
        Page<Blog> blogs = blogService.listBlog(query, pageable);
        if (blogs.getTotalElements() == 0){
            Page<Blog> blogs1 = blogService.listRecommendationsBlog(pageable);
            System.out.println(blogs1.getTotalElements());
            model.addAttribute("recommend",blogs1) ;
        }
        System.out.println("rungging");
        System.out.println(blogs.getTotalElements());
        model.addAttribute("page", blogs);
        model.addAttribute("query",query);
        return "search";
    }

}
