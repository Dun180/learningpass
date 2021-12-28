package com.dun.service.impl;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dun.entity.Answers;
import com.dun.mapper.AnswersMapper;
import com.dun.service.AnswersService;
import org.springframework.stereotype.Service;

@Service("answersService")
public class AnswersServiceImpl extends ServiceImpl<AnswersMapper, Answers> implements AnswersService {
}
