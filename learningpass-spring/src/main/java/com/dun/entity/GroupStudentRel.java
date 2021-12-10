package com.dun.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("group_student_rel")
public class GroupStudentRel {
    private Integer groupId;
    private Integer studentId;
}
