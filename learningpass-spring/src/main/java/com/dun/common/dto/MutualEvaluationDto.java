package com.dun.common.dto;

import com.dun.entity.MutualEvaluationTemplate;
import lombok.Data;

import java.io.Serializable;


@Data
public class MutualEvaluationDto implements Serializable {


    private String taskTitle;
    private MutualEvaluationTemplate template;
}
