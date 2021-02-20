package com.bjpowernode.springboot.blog.service;

import com.bjpowernode.springboot.blog.entity.User;

public interface UserService {

    User checkUser(String username,String password);
}
