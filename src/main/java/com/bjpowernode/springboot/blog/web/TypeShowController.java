package com.bjpowernode.springboot.blog.web;

import com.bjpowernode.springboot.blog.entity.Blog;
import com.bjpowernode.springboot.blog.entity.Type;
import com.bjpowernode.springboot.blog.service.BlogService;
import com.bjpowernode.springboot.blog.service.TypeService;
import com.bjpowernode.springboot.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TypeShowController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/classification/{id}")
    public String types(@PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, @PathVariable Long id, Model model){
        List<Type> typeList = typeService.listTypeTop(10000);
        if (id == -1){
            id = typeList.get(0).getId();
        }
        model.addAttribute("types",typeList);
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);
        Page<Blog> page = blogService.listBlog(pageable, blogQuery);
        model.addAttribute("page",page);
        model.addAttribute("activeTypeId",id);
        if (page.getTotalElements() == 0){
            Page<Blog> blogs1 = blogService.listRecommendationsBlog(pageable);
            model.addAttribute("recommend",blogs1) ;
        }
        return "classification";
    }
}
