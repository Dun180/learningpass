package com.dun.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("question")
public class Question implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer taskId;
    private String stem;
    private Integer score;

}
