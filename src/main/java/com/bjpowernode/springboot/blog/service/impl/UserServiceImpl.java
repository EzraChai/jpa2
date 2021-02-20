package com.bjpowernode.springboot.blog.service.impl;

import com.bjpowernode.springboot.blog.dao.UserRepository;
import com.bjpowernode.springboot.blog.entity.User;
import com.bjpowernode.springboot.blog.service.UserService;
import com.bjpowernode.springboot.blog.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {
        String encryptedPassword = MD5Utils.code(password);
        User user = userRepository.findByUsernameAndPassword(username, encryptedPassword);
        return user;
    }
}
