package com.dun.common.dto;


import lombok.Data;

@Data
public class TaskCompletionDto {


    private Integer taskArrangementId;
    private Integer studentId;
    private String studentNumber;//学号、username
    private String studentName;
    private String taskCompletion;

}
