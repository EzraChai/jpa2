package com.bjpowernode.springboot.blog.dao;

import com.bjpowernode.springboot.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {

    User findByUsernameAndPassword(String username,String password);
}
