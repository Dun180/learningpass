package com.dun.service;

import com.dun.entity.User;
import org.springframework.stereotype.Service;


public interface UserService {
    //根据id查找用户
    public User getUserById(Integer id);
}
