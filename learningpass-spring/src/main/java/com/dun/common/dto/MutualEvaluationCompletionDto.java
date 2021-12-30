package com.dun.common.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MutualEvaluationCompletionDto implements Serializable {


    private Integer templateId;
    private Integer mutualEvaluationId;
    private Integer studentId;
    private String studentNumber;//学号、username
    private String studentName;
    private String mutualEvaluationCompletion;
    private Integer score;
}
