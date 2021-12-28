package com.dun.common.dto;


import com.dun.entity.Question;
import lombok.Data;

import java.io.Serializable;

@Data
public class AnswerDto implements Serializable {
    

    public AnswerDto(Question question){
        taskId = question.getTaskId();
        questionId = question.getId();
        score = question.getScore();
        stem = question.getStem();
        answer = "";
    }
    private Integer taskId;
    private Integer questionId;
    private Integer score;
    private String stem;
    private String answer;

}
