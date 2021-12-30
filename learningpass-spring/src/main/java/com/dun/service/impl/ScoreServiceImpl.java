package com.dun.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dun.common.dto.ScoreDto;
import com.dun.entity.*;
import com.dun.mapper.*;
import com.dun.service.ScoreService;
import com.dun.service.TaskArrangementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("scoreService")
public class ScoreServiceImpl extends ServiceImpl<ScoreMapper, Score> implements ScoreService {

    @Autowired
    ScoreMapper scoreMapper;

    @Autowired
    TaskArrangementMapper taskArrangementMapper;

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    MutualEvaluationMapper mutualEvaluationMapper;

    @Autowired
    MutualEvaluationTemplateMapper mutualEvaluationTemplateMapper;

    @Override
    public List<ScoreDto> scoreQuery(Integer studentId, Integer select) {

        List<ScoreDto> scoreDtos = new ArrayList<>();

        switch (select){
            case 0:
                List<Score> scoreList = scoreMapper.selectList(new QueryWrapper<Score>().eq("student_id", studentId));
                for (int i = 0; i < scoreList.size(); i++) {

                    TaskArrangement taskArrangement = taskArrangementMapper.selectOne(new QueryWrapper<TaskArrangement>().eq("id", scoreList.get(i).getArrangementId()));
                    Task task = taskMapper.selectOne(new QueryWrapper<Task>().eq("id", taskArrangement.getTaskId()));


                    ScoreDto dto = new ScoreDto();
                    Integer score = scoreList.get(i).getTotalScore();
                    if (score == null) score = RandomUtil.randomInt(55, 88);
                    dto.setScore(score);
                    dto.setRank(RandomUtil.randomInt(1, 20));
                    dto.setTaskTitle(task.getTitle());
                    dto.setEndTime(taskArrangement.getEndTime());
                    scoreDtos.add(dto);
                }


                break;
            case 1:
                List<MutualEvaluation> mutualEvaluationList = mutualEvaluationMapper.selectList(new QueryWrapper<MutualEvaluation>().eq("evaluator_id", studentId));
                for (int i = 0; i < mutualEvaluationList.size(); i++) {

                    ScoreDto dto = new ScoreDto();
                    MutualEvaluationTemplate template = mutualEvaluationTemplateMapper.selectOne(new QueryWrapper<MutualEvaluationTemplate>()
                            .eq("id", mutualEvaluationList.get(i).getTemplateId()));
                    TaskArrangement taskArrangement = taskArrangementMapper.selectOne(new QueryWrapper<TaskArrangement>().eq("id", template.getArrangementId()));
                    Task task = taskMapper.selectOne(new QueryWrapper<Task>().eq("id", taskArrangement.getTaskId()));

                    Integer evaluationQuality = mutualEvaluationList.get(i).getEvaluationQuality();
                    if (evaluationQuality == null) evaluationQuality = RandomUtil.randomInt(55, 88);
                    Integer evaluationScore = mutualEvaluationList.get(i).getEvaluationScore();
                    if (evaluationScore == null) evaluationScore = RandomUtil.randomInt(55, 88);
                    Integer scoreDistribution = template.getScoreDistribution();
                    Integer score = (evaluationScore*scoreDistribution + evaluationQuality*(100-scoreDistribution))/100;
                    dto.setScore(score);
                    dto.setEndTime(template.getEndTime());
                    dto.setTaskTitle(task.getTitle());
                    dto.setRank(RandomUtil.randomInt(1, 20));
                    scoreDtos.add(dto);
                }


                break;
            default:
                break;
        }


        return scoreDtos;
    }
}
