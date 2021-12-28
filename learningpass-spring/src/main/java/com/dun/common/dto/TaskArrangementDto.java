package com.dun.common.dto;

import com.dun.entity.TaskArrangement;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TaskArrangementDto implements Serializable {
    public TaskArrangementDto(TaskArrangement taskArrangement){
        id = taskArrangement.getId();
        beginTime = taskArrangement.getBeginTime();
        endTime = taskArrangement.getEndTime();
    }
    private Integer id;
    private String taskTitle;
    private Date beginTime;
    private Date endTime;
}
