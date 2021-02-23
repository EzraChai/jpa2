package com.bjpowernode.springboot.blog.web.admin;

import com.bjpowernode.springboot.blog.entity.Blog;
import com.bjpowernode.springboot.blog.entity.User;
import com.bjpowernode.springboot.blog.service.BlogService;
import com.bjpowernode.springboot.blog.service.TagService;
import com.bjpowernode.springboot.blog.service.TypeService;
import com.bjpowernode.springboot.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT = "/admin/post";
    private static final String LIST = "/admin/admin";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @RequestMapping("/blogs")
    public String list(@PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                       BlogQuery blog, Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        model.addAttribute("types", typeService.listType());
        return LIST;
    }

    @PostMapping("/blogs/search")
    public String searchList(@PageableDefault(size = 2, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                             BlogQuery blog, Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/admin :: blogList";
    }

    @GetMapping("/blogs/post")
    public String inputPage(Model model) {
        setTypeAndTag(model);
        model.addAttribute("blog", new Blog());
        return INPUT;
    }

    @PostMapping("/blogs/add-new")
    public String post(Blog blog, HttpSession session, RedirectAttributes attributes) {
        if (blog.getId() == null){
            Blog blog1 = saveBlog(blog, session);
            if (blog1 == null) {
                attributes.addFlashAttribute("message", "Blog add failed");
            } else {
                attributes.addFlashAttribute("message", "Blog added successfully.");
            }
        }else{
            blog.setUser((User) session.getAttribute("user"));
            blog.setType(typeService.getType(blog.getType().getId()));
            blog.setTags(tagService.listTag(blog.getTagsIds()));
            Blog blog1 = blogService.updateBlog(blog.getId(),blog);
            if (blog1 == null) {
                attributes.addFlashAttribute("message", "Blog update failed");
            } else {
                attributes.addFlashAttribute("message", "Blog update successfully.");
            }
        }

        return REDIRECT_LIST;
    }

    private Blog saveBlog(Blog blog, HttpSession session) {
        if (blog.getFlag().equals("")) {
            blog.setFlag("Original");
        }
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagsIds()));
        Blog blog1 = blogService.saveBlog(blog);
        return blog1;
    }

    private void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
    }

    @GetMapping("/blogs/{id}/edit")
    public String editPage(Model model, @PathVariable Long id) {
        setTypeAndTag(model);
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog", blog);
        return INPUT;
    }

    @GetMapping("/blogs/{id}/delete")
    public String deletePage(RedirectAttributes attributes, @PathVariable Long id) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "Blog deleted successfully.");
        return REDIRECT_LIST;
    }

    @GetMapping("/blogs/avatar")
    public String addAvatar(RedirectAttributes attributes, @RequestParam String avatarPath){
        blogService.addAvatar(avatarPath);
        attributes.addFlashAttribute("message", "Avatar path successfully.");
        return REDIRECT_LIST;
    }

}
