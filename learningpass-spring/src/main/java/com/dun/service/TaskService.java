package com.dun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dun.entity.Task;

import java.util.List;

public interface TaskService extends IService<Task> {



    //添加作业
    public boolean addTask(Task task);
}
