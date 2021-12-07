package com.dun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dun.entity.User;
import com.dun.mapper.UserMapper;
import com.dun.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public User getUserByUsername(String username) {
        User user = null;
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper
                .eq("username",username);
        user = userMapper.selectOne(wrapper);
        System.out.println(user);
        return user;
    }

    @Override
    public User getUserById(Integer id) {
        User user = userMapper.selectById(id);
        return user;
    }
}