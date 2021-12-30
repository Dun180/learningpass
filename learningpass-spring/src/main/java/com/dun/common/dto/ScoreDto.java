package com.dun.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ScoreDto implements Serializable {



    private String taskTitle;
    private Integer score;
    private Integer rank;
    private Date endTime;
}
