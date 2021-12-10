package com.dun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dun.entity.CClass;
import com.dun.mapper.ClassMapper;
import com.dun.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("classService")
public class ClassServiceImpl extends ServiceImpl<ClassMapper, CClass> implements ClassService {

    @Autowired
    private ClassMapper classMapper;

    @Override
    public List<CClass> getClassListByTeacherId(Integer teacherId) {
        QueryWrapper<CClass> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id",teacherId);
        List<CClass> classes = classMapper.selectList(wrapper);
        return classes;
    }

    @Override
    public boolean addClass(CClass cClass) {
        boolean flag = false;

        int insert = classMapper.insert(cClass);
        System.out.println(insert+" "+cClass);
        if(insert>0){
            flag = true;
            System.out.println("add success");
        }else{
            System.out.println("add failed");
        }
        return flag;
    }
}
