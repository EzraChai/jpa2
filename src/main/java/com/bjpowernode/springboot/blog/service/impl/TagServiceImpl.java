package com.bjpowernode.springboot.blog.service.impl;

import com.bjpowernode.springboot.blog.dao.TagRepository;
import com.bjpowernode.springboot.blog.entity.Tag;
import com.bjpowernode.springboot.blog.exception.NotFoundException;
import com.bjpowernode.springboot.blog.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    TagRepository tagRepository;

    @Transactional
    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Transactional
    @Override
    public Tag getTag(Long id) {
        Optional<Tag> byId = tagRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }else{
            throw new NotFoundException("Not Exists.");
        }
    }

    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Optional<Tag> type1 = tagRepository.findById(id);
        if (!type1.isPresent()){
            throw new NotFoundException("Not Exist.");
        }
        BeanUtils.copyProperties(tag,type1.get());
        return tagRepository.save(tag);
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        Optional<Tag> byId = tagRepository.findById(id);
        if (!byId.isPresent()){
            throw new NotFoundException("Not Exist.");
        }
        tagRepository.delete(byId.get());
    }

    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> listTagsTop(Integer size) {
//        Sort sort = new Sort(Sort.Direction.DESC,"blogs.size");
        Sort sort = Sort.by(Sort.Direction.DESC,"blogs.size");
//        Pageable pageable = new PageRequest(0,size,sort);
        Pageable pageable = PageRequest.of(0, size, sort);
        return tagRepository.findTop(pageable);
    }

    @Override
    public List<Tag> listTag(String ids) { //1,3,4

        return tagRepository.findAllById(convertToList(ids));
    }

    private List<Long> convertToList(String ids){
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null){
            Object [] idarray = ids.split(",");
            for (int i = 0; i < idarray.length; i++) {
                try{
                    long l = Long.parseLong((String) idarray[i], 10);
                    list.add(l);
                }catch (NumberFormatException nfe) {
                    Tag tag = new Tag();
                    tag.setName((String) idarray[i]);
                    Tag tag1 = saveTag(tag);
                    list.add(tag1.getId());
                }
            }
        }
        return list;
    }
}
