package com.dun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dun.entity.Task;
import com.dun.mapper.TaskMapper;
import com.dun.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("taskService")
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {
    @Autowired
    TaskMapper taskMapper;


    @Override
    public boolean addTask(Task task) {
        boolean flag = false;
        int insert = taskMapper.insert(task);
        System.out.println(insert+" "+task);
        if(insert>0){
            flag = true;
            System.out.println("add success");
        }else{
            System.out.println("add failed");
        }
        return flag;
    }
}
