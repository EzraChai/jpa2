package com.bjpowernode.springboot.blog.service;

import com.bjpowernode.springboot.blog.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {

    Tag getTagByName(String name);

    Tag saveTag(Tag tag);

    Tag getTag(Long id);

    Page<Tag> listTag(Pageable pageable);

    Tag updateTag(Long id, Tag tag);

    void deleteTag(Long id);

    List<Tag>listTag();

    List<Tag>listTag(String ids);
}
