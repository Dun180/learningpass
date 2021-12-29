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
@TableName("mutual_evaluation")
public class MutualEvaluation implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer evaluatorId;
    private Integer answerId;
    private Integer templateId;
    private Integer evaluationScore;
    private Integer evaluationQuality;
    private Integer state;


}
