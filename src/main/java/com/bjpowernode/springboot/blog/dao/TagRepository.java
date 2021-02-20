package com.bjpowernode.springboot.blog.dao;

import com.bjpowernode.springboot.blog.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TagRepository extends JpaRepository<Tag,Long>, JpaSpecificationExecutor<Tag> {
    Tag findByName(String name);
}
