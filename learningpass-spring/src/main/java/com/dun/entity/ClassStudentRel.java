package com.dun.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("class_student_rel")
public class ClassStudentRel implements Serializable {
    private Integer classId;
    private Integer studentId;

}
