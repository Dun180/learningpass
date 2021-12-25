package com.dun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dun.entity.Question;
import com.dun.mapper.QuestionMapper;
import com.dun.service.QuestionService;
import org.springframework.stereotype.Service;

@Service("questionService")
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper,Question> implements QuestionService {
}
