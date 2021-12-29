package com.dun.service.impl;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dun.common.dto.*;
import com.dun.entity.*;
import com.dun.mapper.*;
import com.dun.service.MutualEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("mutualEvaluationService")
public class MutualEvaluationServiceImpl extends ServiceImpl<MutualEvaluationMapper, MutualEvaluation> implements MutualEvaluationService {

    @Autowired
    MutualEvaluationTemplateMapper mutualEvaluationTemplateMapper;

    @Autowired
    MutualEvaluationMapper mutualEvaluationMapper;

    @Autowired
    EvaluateAnswersMapper evaluateAnswersMapper;

    @Autowired
    ScoreMapper scoreMapper;

    @Autowired
    AnswerMapper answerMapper;

    @Autowired
    AnswersMapper answersMapper;

    @Autowired
    TaskArrangementMapper taskArrangementMapper;

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionMapper questionMapper;

    @Override
    public boolean createMutualEvaluationTask(Integer arrangementId, Integer gradeMode, Integer scoreDistribution, Date beginTime,Date endTime) {
        //创建互评模板
        MutualEvaluationTemplate template = new MutualEvaluationTemplate();
        template.setArrangementId(arrangementId);
        template.setScoreDistribution(scoreDistribution);
        template.setGradeMode(gradeMode);
        template.setBeginTime(beginTime);
        template.setEndTime(endTime);
        if(mutualEvaluationTemplateMapper.insert(template)==0) return false;

        //获取score和answer
        List<Score> scoreList = scoreMapper.selectList(new QueryWrapper<Score>().eq("arrangement_id", arrangementId));
        List<Answer> answerList = new ArrayList<>();
        for (int i = 0; i < scoreList.size(); i++) {
            Answer answer = answerMapper.selectOne(new QueryWrapper<Answer>().eq("score_id", scoreList.get(i).getId()));
            answerList.add(answer);
        }

        for (int i = 0; i < scoreList.size(); i++) {
            Answer answer = null;
            //防止自己批到自己的作业
            while (true){
                answer = RandomUtil.randomEle(answerList);
                if (answer.getScoreId() != scoreList.get(i).getId()) break;
            }
            if(!answerList.remove(answer)) return false;
            //创建互评
            MutualEvaluation mutualEvaluation = new MutualEvaluation();
            mutualEvaluation.setEvaluatorId(scoreList.get(i).getStudentId());
            mutualEvaluation.setAnswerId(answer.getId());
            mutualEvaluation.setTemplateId(template.getId());
            mutualEvaluation.setState(0);
            if(mutualEvaluationMapper.insert(mutualEvaluation) == 0) return false;

            //创建evaluate_answers
            List<Answers> answersList = answersMapper.selectList(new QueryWrapper<Answers>().eq("answer_id", answer.getId()));
            for (int j = 0; j < answersList.size(); j++) {
                EvaluateAnswers evaluateAnswers = new EvaluateAnswers();
                evaluateAnswers.setAnswersId(answersList.get(j).getId());
                evaluateAnswers.setMutualEvaluationId(mutualEvaluation.getId());
                if(evaluateAnswersMapper.insert(evaluateAnswers)==0) return false;
            }
        }

        return true;
    }

    @Override
    public List<MutualEvaluationDto> getMutualEvaluationList(Integer classId) {
        List<MutualEvaluationDto> dtoList = new ArrayList<>();

        List<TaskArrangement> taskArrangementList = taskArrangementMapper.selectList(new QueryWrapper<TaskArrangement>().eq("class_id", classId));

        for (int i = 0; i < taskArrangementList.size(); i++) {
            List<MutualEvaluationTemplate> templateList = mutualEvaluationTemplateMapper.selectList(new QueryWrapper<MutualEvaluationTemplate>().eq("arrangement_id", taskArrangementList.get(i).getId()));
            String taskTitle = taskMapper.selectOne(new QueryWrapper<Task>().eq("id",taskArrangementList.get(i).getTaskId())).getTitle();
            for (int j = 0; j < templateList.size(); j++) {
                MutualEvaluationDto mutualEvaluationDto = new MutualEvaluationDto();
                mutualEvaluationDto.setTemplate(templateList.get(j));
                mutualEvaluationDto.setTaskTitle(taskTitle);
                dtoList.add(mutualEvaluationDto);
            }

        }

        return dtoList;
    }

    @Override
    public List<MutualEvaluationCompletionDto> getMutualEvaluationCompletion(Integer templateId) {
        List<MutualEvaluationCompletionDto> dtoList = new ArrayList<>();

        //获取互评列表

        List<MutualEvaluation> mutualEvaluationList = mutualEvaluationMapper.selectList(new QueryWrapper<MutualEvaluation>().eq("template_id", templateId));


        //根据每一个互评表的表项建立taskCompletion
        for (int i = 0; i < mutualEvaluationList.size(); i++) {
            User student = userMapper.selectOne(new QueryWrapper<User>().eq("id", mutualEvaluationList.get(i).getEvaluatorId()));
            MutualEvaluationCompletionDto dto = new MutualEvaluationCompletionDto();
            dto.setStudentId(student.getId());
            dto.setStudentNumber(student.getUsername());
            dto.setStudentName(student.getName());
            dto.setTemplateId(templateId);
            dto.setMutualEvaluationId(mutualEvaluationList.get(i).getId());
            switch (mutualEvaluationList.get(i).getState()){
                case 0:
                    dto.setMutualEvaluationCompletion("未完成");
                    break;
                case 1:
                    dto.setMutualEvaluationCompletion("已提交");
                    break;
                case 2:
                    dto.setMutualEvaluationCompletion("已批阅");
                    break;
                default:
                    break;
            }
            dtoList.add(dto);

        }


        return dtoList;
    }

    @Override
    public List<MutualEvaluationDto> getmutualEvaluationListByStudentId(Integer studentId) {
        List<MutualEvaluationDto> dtoList = new ArrayList<>();

        List<MutualEvaluation> mutualEvaluationList = mutualEvaluationMapper.selectList(new QueryWrapper<MutualEvaluation>().eq("evaluator_id", studentId));

        for (int i = 0; i < mutualEvaluationList.size(); i++) {

            MutualEvaluationTemplate template = mutualEvaluationTemplateMapper.selectOne(new QueryWrapper<MutualEvaluationTemplate>().eq("id", mutualEvaluationList.get(i).getTemplateId()));
            TaskArrangement taskArrangement = taskArrangementMapper.selectOne(new QueryWrapper<TaskArrangement>().eq("id", template.getArrangementId()));
            Task task = taskMapper.selectOne(new QueryWrapper<Task>().eq("id", taskArrangement.getTaskId()));

            MutualEvaluationDto dto = new MutualEvaluationDto();
            dto.setTemplate(template);
            dto.setTaskTitle(task.getTitle());
            dtoList.add(dto);

        }



        return dtoList;
    }

    @Override
    public JSONObject getMutualEvaluationInfo(Integer studentId, Integer templateId) {
        MutualEvaluation mutualEvaluation = mutualEvaluationMapper.selectOne(new QueryWrapper<MutualEvaluation>()
                .eq("evaluator_id", studentId)
                .eq("template_id", templateId)
        );
        Answer answer = answerMapper.selectOne(new QueryWrapper<Answer>().eq("id", mutualEvaluation.getAnswerId()));

        Score score = scoreMapper.selectOne(new QueryWrapper<Score>().eq("id", answer.getScoreId()));
        TaskArrangement ta = taskArrangementMapper.selectOne(new QueryWrapper<TaskArrangement>()
                .eq("id", score.getArrangementId())
        );
        Task task = taskMapper.selectOne(new QueryWrapper<Task>().eq("id", ta.getTaskId()));
        List<Question> questionList = questionMapper.selectList(new QueryWrapper<Question>().eq("task_id", task.getId()));

        List<AnswerDto> answerDtoList = new ArrayList<>();

        for (int i = 0; i < questionList.size(); i++) {

            Answers answers = answersMapper.selectOne(new QueryWrapper<Answers>()
                    .eq("answer_id", answer.getId())
                    .eq("question_id", questionList.get(i).getId())
            );

            EvaluateAnswers evaluateAnswers = evaluateAnswersMapper.selectOne(new QueryWrapper<EvaluateAnswers>()
                    .eq("answers_id", answers.getId())
                    .eq("mutual_evaluation_id", mutualEvaluation.getId())
            );

            AnswerDto answerDto = new AnswerDto(questionList.get(i));
            answerDto.setAnswer(answers.getAnswer());
            answerDto.setActualScore(evaluateAnswers.getScore());
            answerDto.setEvaluation(evaluateAnswers.getEvaluation());
            answerDtoList.add(answerDto);
        }


        JSONObject json = JSONUtil.createObj()
                .set("title",task.getTitle())
                .set("answerId",answer.getId())
                .set("answerDtoList",answerDtoList)
                .set("evaluationQuality",mutualEvaluation.getEvaluationQuality())
                ;
        return json;
    }

    @Override
    public boolean submitEvaluation(Integer templateId,Integer answerId, List<AnswerDto> answerDtoList) {
        //更新评价状态
        Answer answer = answerMapper.selectOne(new QueryWrapper<Answer>().eq("id", answerId));

        MutualEvaluation mutualEvaluation = mutualEvaluationMapper.selectOne(new QueryWrapper<MutualEvaluation>()
                .eq("answer_id", answer.getId())
                .eq("template_id",templateId)
        );
        mutualEvaluation.setState(1);
        if (mutualEvaluationMapper.updateById(mutualEvaluation)==0) return false;

        //更新evaluate_answers内容
        for (int i = 0; i < answerDtoList.size(); i++) {

            Answers answers = answersMapper.selectOne(new QueryWrapper<Answers>()
                    .eq("question_id", answerDtoList.get(i).getQuestionId())
                    .eq("answer_id", answerId)
            );

            EvaluateAnswers evaluateAnswers = evaluateAnswersMapper.selectOne(new QueryWrapper<EvaluateAnswers>()
                    .eq("answers_id", answers.getId())
                    .eq("mutual_evaluation_id", mutualEvaluation.getId())
            );
            evaluateAnswers.setScore(answerDtoList.get(i).getActualScore());
            evaluateAnswers.setEvaluation(answerDtoList.get(i).getEvaluation());

            if (evaluateAnswersMapper.update(evaluateAnswers,new UpdateWrapper<EvaluateAnswers>()
                    .eq("answers_id",evaluateAnswers.getAnswersId())
                    .eq("mutual_evaluation_id",evaluateAnswers.getMutualEvaluationId())
            )==0) return false;
        }
        return true;
    }
}
