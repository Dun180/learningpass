package com.dun.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dun.common.dto.AnswerDto;
import com.dun.common.dto.MutualEvaluationCompletionDto;
import com.dun.common.dto.MutualEvaluationDto;
import com.dun.common.dto.TaskCompletionDto;
import com.dun.entity.MutualEvaluation;
import lombok.Data;

import java.util.Date;
import java.util.List;

public interface MutualEvaluationService extends IService<MutualEvaluation> {


    //创建互评作业
    public boolean createMutualEvaluationTask(Integer arrangementId, Integer gradeMode, Integer scoreDistribution, Date beginTime, Date endTime);

    //根据班级id获取作业互评列表
    public List<MutualEvaluationDto> getMutualEvaluationList(Integer classId);

    //获取作业互评完成情况
    public List<MutualEvaluationCompletionDto> getMutualEvaluationCompletion(Integer templateId);

    //根据学生id获取作业互评列表
    public List<MutualEvaluationDto> getmutualEvaluationListByStudentId(Integer studentId);

    //获取互评作业详情
    public JSONObject getMutualEvaluationInfo(Integer studentId,Integer templateId);

    //提交互评作业
    public boolean submitEvaluation(Integer templateId,Integer answerId,List<AnswerDto> answerDtoList);
}
