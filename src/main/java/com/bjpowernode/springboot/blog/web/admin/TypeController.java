package com.bjpowernode.springboot.blog.web.admin;

import com.bjpowernode.springboot.blog.entity.Type;
import com.bjpowernode.springboot.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @GetMapping("/types")
    public String types(@PageableDefault(size = 10,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable, Model model){
        Page<Type> types = typeService.listType(pageable);
        model.addAttribute("page",types);
        return "admin/types";
    }

    @GetMapping("/types/add-new")
    public String input(Model model){
        model.addAttribute("type",new Type());
        System.out.println(model.getAttribute("type"));
        return "/admin/types-input";
    }

    @GetMapping("/types/{id}/edit")
    public String input(@PathVariable Long id ,Model model){
        Type type = typeService.getType(id);
        model.addAttribute(type);
        return "/admin/edit";
    }

    @PostMapping("/types")
    public String post(@Valid Type type, BindingResult result, RedirectAttributes redirectAttributes){
        Type type2 = typeService.getTypeByName(type.getName());
        if (type2 != null){
            result.rejectValue("name","nameError","Classification's name repeated.");
        }
        if (result.hasErrors()){
            return "admin/types/add-new";
        }
        System.out.println(type.toString());
        Type type1 = typeService.saveType(type);
        if (type1 == null){
            //
            redirectAttributes.addFlashAttribute("message","Opps, something went wrong.");
        }else{
            //
            redirectAttributes.addFlashAttribute("message","Congratulation, it added Successfully");
        }
        return "redirect:/admin/types";
    }

    @PostMapping("/types/{id}")
    public String editpost(@Valid Type type, BindingResult result,@PathVariable Long id, RedirectAttributes redirectAttributes){
        Type type2 = typeService.getTypeByName(type.getName());
        if (type2 != null){
            result.rejectValue("name","nameError","Classification's name repeated.");
        }
        if (result.hasErrors()){
            return "admin/types/{id}/edit";
        }
        System.out.println(type.toString());
        Type type1 = typeService.updateType(id,type);
        if (type1 == null){
            //
            redirectAttributes.addFlashAttribute("message","Ops, something went wrong.");
        }else{
            //
            redirectAttributes.addFlashAttribute("message","Congratulation, it changed Successfully.");
        }
        return "redirect:/admin/types";
    }

    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable("id") Long id){
        System.out.println(id);
        typeService.deleteType(id);
        return "redirect:/admin/types";
    }
}
