package com.dun.common.dto;

import com.dun.entity.TaskArrangement;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TaskArrangementDto implements Serializable {
    public TaskArrangementDto(Integer arrangementId, String taskTitle, Date beginTime, Date endTime) {
        this.arrangementId = arrangementId;
        this.taskTitle = taskTitle;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    public TaskArrangementDto(TaskArrangement taskArrangement){
        arrangementId = taskArrangement.getId();
        beginTime = taskArrangement.getBeginTime();
        endTime = taskArrangement.getEndTime();
    }
    private Integer arrangementId;
    private String taskTitle;
    private Date beginTime;
    private Date endTime;
}
