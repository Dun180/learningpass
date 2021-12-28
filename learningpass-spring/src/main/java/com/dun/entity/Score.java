package com.dun.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("score")
public class Score implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private Integer studentId;
    private Integer groupId;
    private Integer arrangementId;
    private Integer totalScore;
    private Integer flag;
}
