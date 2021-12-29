package com.dun.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mutual_evaluation_template")
public class MutualEvaluationTemplate implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer arrangementId;
    private Integer scoreDistribution;
    private Integer gradeMode;
    private Date beginTime;
    private Date endTime;

}
