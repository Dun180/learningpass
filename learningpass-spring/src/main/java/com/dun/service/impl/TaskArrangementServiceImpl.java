package com.dun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dun.common.dto.TaskArrangementDto;
import com.dun.common.dto.TaskCompletionDto;
import com.dun.entity.*;
import com.dun.mapper.*;
import com.dun.service.TaskArrangementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("taskArrangementService")
public class TaskArrangementServiceImpl extends ServiceImpl<TaskArrangementMapper, TaskArrangement> implements TaskArrangementService {
    @Autowired
    TaskMapper taskMapper;

    @Autowired
    TaskArrangementMapper taMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ScoreMapper scoreMapper;

    @Autowired
    AnswerMapper answerMapper;

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    AnswersMapper answersMapper;

    @Override
    public boolean taskArrangement(Integer classId, Integer taskId, Integer mode, Date beginTime, Date endTime) {

        switch (mode){
            case 1://小组布置
            case 2://抢答布置
            case 0://学生布置
                //创建作业布置
                TaskArrangement ta = new TaskArrangement();
                ta.setTaskId(taskId);
                ta.setClassId(classId);
                ta.setBeginTime(beginTime);
                ta.setEndTime(endTime);
                if(taMapper.insert(ta)==0) return false;

                //根据班级id查出所有学生的id，并一一建立对应的score和answer
                //获取班级全部成员
                List<User> memberList = userMapper.selectList(new QueryWrapper<User>()
                        .select("id")
                        .inSql("id", "select student_id as id from class_student_rel where class_id ='" + classId + "'"));

                //根据作业id查出问题列表
                List<Question> questionList = questionMapper.selectList(new QueryWrapper<Question>().eq("task_id", taskId));


                for (int i = 0; i < memberList.size(); i++) {
                    //创建score记录
                    Score score = new Score();
                    score.setFlag(0);
                    score.setStudentId(memberList.get(i).getId());
                    score.setArrangementId(ta.getId());
                    if (scoreMapper.insert(score)==0) return false;
                    //创建answer记录
                    Answer answer = new Answer();
                    answer.setScoreId(score.getId());
                    answer.setState(0);
                    if (answerMapper.insert(answer)==0) return false;
                    //对应作业中的每个题目创建answers
                    for (int j = 0; j < questionList.size(); j++) {
                        Answers answers = new Answers();
                        answers.setAnswerId(answer.getId());
                        answers.setQuestionId(questionList.get(j).getId());
                        //System.out.println("insert: "+answers);
                        if (answersMapper.insert(answers)==0) return false;
                    }
                }

                break;

            default:
                break;
        }

        return true;
    }

    @Override
    public List<TaskArrangementDto> getTaskByStudentId(Integer studentId) {
        List<TaskArrangementDto> dtoList = new ArrayList<>();

        //根据学生id查出成绩列表
        List<Score> scoreList = scoreMapper.selectList(new QueryWrapper<Score>().eq("student_id", studentId));

        for (int i = 0; i < scoreList.size(); i++) {
            //根据成绩列表中的arrangement_id查询
            TaskArrangement ta = taMapper.selectOne(new QueryWrapper<TaskArrangement>().eq("id", scoreList.get(i).getArrangementId()));
            //根据taskArrangement中的task_id查出task的title
            String taskTitle = taskMapper.selectOne(new QueryWrapper<Task>().eq("id",ta.getTaskId())).getTitle();
            //构造dto
            TaskArrangementDto taDto = new TaskArrangementDto(ta);
            taDto.setTaskTitle(taskTitle);

            dtoList.add(taDto);

        }


        return dtoList;
    }

    @Override
    public List<TaskCompletionDto> getTaskCompletion(Integer arrangementId) {

        List<TaskCompletionDto> taskCompletionDtoList = new ArrayList<>();

        //获取分数列表
        List<Score> scoreList = scoreMapper.selectList(new QueryWrapper<Score>().eq("arrangement_id", arrangementId));

        //根据每一个分数表的表项建立taskCompletion
        for (int i = 0; i < scoreList.size(); i++) {
            Answer answer = answerMapper.selectOne(new QueryWrapper<Answer>().eq("score_id", scoreList.get(i).getId()));
            User student = userMapper.selectOne(new QueryWrapper<User>().eq("id", scoreList.get(i).getStudentId()));
            TaskCompletionDto taskCompletionDto = new TaskCompletionDto();
            taskCompletionDto.setStudentId(student.getId());
            taskCompletionDto.setStudentNumber(student.getUsername());
            taskCompletionDto.setStudentName(student.getName());
            taskCompletionDto.setTaskArrangementId(arrangementId);
            switch (answer.getState()){
                case 0:
                    taskCompletionDto.setTaskCompletion("未完成");
                    break;
                case 1:
                    taskCompletionDto.setTaskCompletion("已提交");
                    break;
                case 2:
                    taskCompletionDto.setTaskCompletion("已批阅");
                    break;
                default:
                    break;
            }
            taskCompletionDtoList.add(taskCompletionDto);

        }


        return taskCompletionDtoList;
    }
}
