package com.dun.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("evaluate_answers")
public class EvaluateAnswers implements Serializable {

    private Integer answersId;
    private Integer mutualEvaluationId;
    private Integer score;
    private String evaluation;
}
