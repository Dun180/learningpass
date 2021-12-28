package com.dun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dun.common.dto.AnswerDto;
import com.dun.entity.Answer;

import java.util.List;

public interface AnswerService extends IService<Answer> {

    public Boolean submitAnswer(Integer answerId,List<AnswerDto> answerDtoList);
}
