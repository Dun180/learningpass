package com.dun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dun.entity.ClassStudentRel;
import com.dun.mapper.ClassStudentRelMapper;
import com.dun.service.ClassStudentRelService;
import org.springframework.stereotype.Service;

@Service("classStudentRelService")
public class ClassStudentRelServiceImpl extends ServiceImpl<ClassStudentRelMapper, ClassStudentRel> implements ClassStudentRelService {
}
