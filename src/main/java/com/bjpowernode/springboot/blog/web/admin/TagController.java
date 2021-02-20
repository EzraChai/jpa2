package com.bjpowernode.springboot.blog.web.admin;

import com.bjpowernode.springboot.blog.entity.Tag;
import com.bjpowernode.springboot.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagService typeService;

    @GetMapping("/tags")
    public String tags(@PageableDefault(size = 10,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable, Model model){
        Page<Tag> types = typeService.listTag(pageable);
        model.addAttribute("page",types);
        return "admin/tags";
    }

    @GetMapping("/tags/add-new")
    public String input(Model model){
        model.addAttribute("type",new Tag());
        System.out.println(model.getAttribute("type"));
        return "/admin/tags-input";
    }

    @GetMapping("/tags/{id}/edit")
    public String input(@PathVariable Long id , Model model){
        Tag tag = typeService.getTag(id);
        System.out.println(tag);
        model.addAttribute(tag);
        return "/admin/tags-edit";
    }

    @PostMapping("/tags")
    public String post(@Valid Tag type, BindingResult result, RedirectAttributes redirectAttributes){
        Tag type2 = typeService.getTagByName(type.getName());
        if (type2 != null){
            result.rejectValue("name","nameError","Tags's name repeated.");
        }
        if (result.hasErrors()){
            return "admin/tags/add-new";
        }
        Tag type1 = typeService.saveTag(type);
        if (type1 == null){
            //
            redirectAttributes.addFlashAttribute("message","Opps, something went wrong.");
        }else{
            //
            redirectAttributes.addFlashAttribute("message","Congratulation, it added Successfully");
        }
        return "redirect:/admin/tags";
    }

    @PostMapping("/tags/{id}")
    public String editpost(@Valid Tag type, BindingResult result,@PathVariable Long id, RedirectAttributes redirectAttributes){
        Tag type2 = typeService.getTagByName(type.getName());
        if (type2 != null){
            result.rejectValue("name","nameError","Tag's name repeated.");
        }
        if (result.hasErrors()){
            return "admin/tags/{id}/edit";
        }
        System.out.println("Working "+type.toString());
        Tag type1 = typeService.updateTag(id,type);
        if (type1 == null){
            //
            redirectAttributes.addFlashAttribute("message","Ops, something went wrong.");
        }else{
            //
            redirectAttributes.addFlashAttribute("message","Congratulation, it changed Successfully.");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable("id") Long id){
        System.out.println(id);
        typeService.deleteTag(id);
        return "redirect:/admin/tags";
    }
}
