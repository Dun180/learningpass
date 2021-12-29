package com.dun.common.dto;


import com.dun.entity.Question;
import lombok.Data;

import java.io.Serializable;

@Data
public class AnswerDto implements Serializable {
    public AnswerDto(Integer taskId, Integer questionId, Integer score, Integer actualScore, String stem, String answer, String evaluation) {
        this.taskId = taskId;
        this.questionId = questionId;
        this.score = score;
        this.actualScore = actualScore;
        this.stem = stem;
        this.answer = answer;
        this.evaluation = evaluation;
    }

    public AnswerDto(Question question){
        taskId = question.getTaskId();
        questionId = question.getId();
        score = question.getScore();
        stem = question.getStem();
        answer = "";
    }
    private Integer taskId;
    private Integer questionId;
    private Integer score;//满分
    private Integer actualScore;//实际得分
    private String stem;
    private String answer;
    private String evaluation;

}
