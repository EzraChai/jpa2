package com.bjpowernode.springboot.blog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {

    @GetMapping("/about-me")
    public String about(){
        return "profile";
    }
}
