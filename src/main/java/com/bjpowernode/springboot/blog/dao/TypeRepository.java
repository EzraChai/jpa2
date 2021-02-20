package com.bjpowernode.springboot.blog.dao;

import com.bjpowernode.springboot.blog.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TypeRepository extends JpaRepository<Type,Long>, JpaSpecificationExecutor<Type> {

    Type findByName(String name);
}
