package com.dun.service;

import com.dun.entity.User;
import org.springframework.stereotype.Service;


public interface UserService {

    //根据用户名查找用户
    public User getUserByUsername(String username);

    //根据id查找用户
    public User getUserById(Integer id);
}
