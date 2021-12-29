package com.dun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dun.common.dto.AnswerDto;
import com.dun.entity.Answer;
import com.dun.entity.Answers;
import com.dun.mapper.AnswerMapper;
import com.dun.mapper.AnswersMapper;
import com.dun.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("answerService")
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, Answer> implements AnswerService {

    @Autowired
    AnswerMapper answerMapper;

    @Autowired
    AnswersMapper answersMapper;

    @Override
    public boolean submitAnswer(Integer answerId, List<AnswerDto> answerDtoList) {

        //更新回答状态
        Answer answer = answerMapper.selectOne(new QueryWrapper<Answer>().eq("id", answerId));
        answer.setState(1);
        if (answerMapper.updateById(answer)==0) return false;

        //更新answers内容
        for (int i = 0; i < answerDtoList.size(); i++) {
            //根据answerId和questionId查出answers
            Answers answers = answersMapper.selectOne(new QueryWrapper<Answers>()
                    .eq("question_id", answerDtoList.get(i).getQuestionId())
                    .eq("answer_id", answerId)
            );
            answers.setAnswer(answerDtoList.get(i).getAnswer());
            if (answersMapper.update(answers,new UpdateWrapper<Answers>()
                    .eq("question_id",answers.getQuestionId())
                    .eq("answer_id",answers.getAnswerId())
            )==0) return false;
        }
        return true;
    }

    @Override
    public boolean submitGrade(Integer answerId, List<AnswerDto> answerDtoList) {
        //更新回答状态
        Answer answer = answerMapper.selectOne(new QueryWrapper<Answer>().eq("id", answerId));
        answer.setState(2);
        if (answerMapper.updateById(answer)==0) return false;

        //更新answers内容
        for (int i = 0; i < answerDtoList.size(); i++) {
            //根据answerId和questionId查出answers
            Answers answers = answersMapper.selectOne(new QueryWrapper<Answers>()
                    .eq("question_id", answerDtoList.get(i).getQuestionId())
                    .eq("answer_id", answerId)
            );
            answers.setScore(answerDtoList.get(i).getActualScore());
            if (answersMapper.update(answers,new UpdateWrapper<Answers>()
                    .eq("question_id",answers.getQuestionId())
                    .eq("answer_id",answers.getAnswerId())
            )==0) return false;
        }
        return true;
    }
}
