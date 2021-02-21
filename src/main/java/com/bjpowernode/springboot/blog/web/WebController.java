package com.bjpowernode.springboot.blog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @Deprecated
    @GetMapping("/x")
    public String index() {
        System.out.println("==========index Method============");
        return "index";
    }

    @GetMapping("/achieve")
    public String archieve() {
        return "achieve";
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

    @GetMapping("/classification")
    public String classification() {
        return "classification";
    }

    @GetMapping("/tags")
    public String tags() {
        return "tags";
    }

    @GetMapping("/blog")
    public String blog() {
        return "blog";
    }

}
