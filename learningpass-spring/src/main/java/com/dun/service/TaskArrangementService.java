package com.dun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dun.common.dto.TaskArrangementDto;
import com.dun.common.dto.TaskCompletionDto;
import com.dun.entity.TaskArrangement;

import java.util.Date;
import java.util.List;

public interface TaskArrangementService extends IService<TaskArrangement> {

    //布置作业
    public boolean taskArrangement(Integer classId, Integer taskId, Integer mode, Date beginTime, Date endTime);

    //根据学生id获取task列表
    public List<TaskArrangementDto> getTaskByStudentId(Integer studentId);

    //获取作业完成情况
    public List<TaskCompletionDto> getTaskCompletion(Integer arrangementId);
}
