package com.dun.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("answers")
public class Answers {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private Integer questionId;

    private Integer answerId;
    private Integer score;

    private String answer;


}
