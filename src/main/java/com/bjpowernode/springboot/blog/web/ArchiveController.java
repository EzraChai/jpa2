package com.bjpowernode.springboot.blog.web;

import com.bjpowernode.springboot.blog.entity.Blog;
import com.bjpowernode.springboot.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class ArchiveController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/archives")
    public String archive(Model model){
        Map<String, List<Blog>> stringListMap = blogService.archieveBlog();
        model.addAttribute("archiveMap" , stringListMap);
        model.addAttribute("blogCount",blogService.countBlog());
        return "achieve";
    }
}
