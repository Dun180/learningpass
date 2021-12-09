package com.dun.service;

import com.dun.entity.CClass;

import java.util.List;

public interface ClassService {

    //根据教师ID查询班级列表
    public List<CClass> getClassListByTeacherId(Integer teacherId);

    //添加班级
    public boolean addClass(CClass cClass);
}
