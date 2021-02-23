package com.bjpowernode.springboot.blog.dao;

import com.bjpowernode.springboot.blog.entity.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvatarRepository extends JpaRepository<Avatar,Long> {
}
