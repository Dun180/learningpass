package com.dun.common.dto;


import lombok.Data;

import java.io.Serializable;

@Data
public class TaskCompletionDto implements Serializable {


    private Integer taskArrangementId;
    private Integer studentId;
    private String studentNumber;//学号、username
    private String studentName;
    private String taskCompletion;
    private Integer score;

}
