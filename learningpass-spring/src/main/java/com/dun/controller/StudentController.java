package com.dun.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dun.common.dto.AnswerDto;
import com.dun.common.dto.MutualEvaluationDto;
import com.dun.common.dto.TaskArrangementDto;
import com.dun.common.lang.Result;
import com.dun.entity.*;
import com.dun.service.*;
import com.dun.util.DunUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    ClassService classService;

    @Autowired
    ClassStudentRelService csrService;

    @Autowired
    TaskArrangementService taService;

    @Autowired
    TaskService taskService;

    @Autowired
    QuestionService questionService;

    @Autowired
    ScoreService scoreService;

    @Autowired
    AnswerService answerService;

    @Autowired
    AnswersService answersService;

    @Autowired
    MutualEvaluationService mutualEvaluationService;

    //获取班级列表
    @GetMapping("/classes/{id}")
    public Result getClassesByStudentId(@PathVariable("id") Integer studentId,@RequestParam(defaultValue = "1") Integer currentPage) {

        //List<CClass> cClasses = classService.getClassListByTeacherId(id);
        //分页

        System.out.println("id:"+studentId+" page:"+currentPage);
        if(currentPage == null || currentPage < 1) currentPage = 1;
        Page page = new Page<>(currentPage,6);
        IPage pageData = classService.page(page, new QueryWrapper<CClass>()
                .inSql("id","select class_id as id from class_student_rel where student_id ='"+studentId+"'")
        );
        System.out.println(pageData.getRecords());
        return Result.succ(pageData);
    }

    //加入班级
    @PostMapping("/class:")
    public Result joinClass(@RequestBody Map<String,Object> map){
        System.out.println(map);
        ClassStudentRel csr = new ClassStudentRel();
        csr.setStudentId(Integer.parseInt(map.get("studentId").toString()));

        CClass cClass = classService.getOne(new QueryWrapper<CClass>().eq("code", map.get("code").toString()));
        if(cClass!=null){
            csr.setClassId(cClass.getId());
        }else {
            return Result.fail("班级代码错误");
        }

        if (csrService.save(csr)){
            return Result.succ(MapUtil.builder()
                    .put("addResult",true)
                    .map()
            );
        }else {
            return Result.fail("加入失败");
        }

    }

    //获取作业列表
    @GetMapping("/{id}/taskList")
    public Result getTasksByStudentId(@PathVariable("id") Integer id){
        List<TaskArrangementDto> list = taService.getTaskByStudentId(id);
        return Result.succ(list);
    }

    //获取作业详情
    @GetMapping("/answer")
    public Result getTaskInfo(@RequestParam(value = "studentId",required=true) Integer studentId,@RequestParam(value = "arrangementId",required=true) Integer arrangementId){


        Score score = scoreService.getOne(new QueryWrapper<Score>()
                .eq("student_id", studentId)
                .eq("arrangement_id", arrangementId)
        );
        if (score == null) return Result.fail("获取错误");
        Answer answer = answerService.getOne(new QueryWrapper<Answer>().eq("score_id",score.getId()));

        TaskArrangement ta = taService.getOne(new QueryWrapper<TaskArrangement>()
                .eq("id", score.getArrangementId())
        );
        Task task = taskService.getOne(new QueryWrapper<Task>().eq("id", ta.getTaskId()));
        List<Question> questionList = questionService.list(new QueryWrapper<Question>().eq("task_id", task.getId()));

        List<AnswerDto> answerDtoList = new ArrayList<>();

        for (int i = 0; i < questionList.size(); i++) {

            Answers answers = answersService.getOne(new QueryWrapper<Answers>()
                    .eq("answer_id", answer.getId())
                    .eq("question_id", questionList.get(i).getId())
            );
            AnswerDto answerDto = new AnswerDto(questionList.get(i));
            answerDto.setAnswer(answers.getAnswer());
            answerDto.setActualScore(answers.getScore());
            answerDtoList.add(answerDto);
        }


        JSONObject json = JSONUtil.createObj()
                .set("title",task.getTitle())
                .set("answerId",answer.getId())
                .set("answerDtoList",answerDtoList)
                ;


        return Result.succ(json);

    }

    //提交作业
    @PostMapping("/submitAnswer")
    public Result submitAnswer(@RequestBody String str){


        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(str);
        String answerDtoListStr = jsonObject.getString("answerDtoList");

        List<AnswerDto> answerDtoList = com.alibaba.fastjson.JSONObject.parseArray(answerDtoListStr, AnswerDto.class);
        Integer answerId = jsonObject.getInteger("answerId");
        System.out.println(answerDtoList);

        if (answerService.submitAnswer(answerId,answerDtoList)){
            return Result.succ(true);
        }else {
            return Result.fail("提交失败");
        }
    }

    //获取互评作业列表
    @GetMapping("/{id}/mutualEvaluationList")
    public Result getMutualEvaluationListByStudentId(@PathVariable("id") Integer id){

        List<MutualEvaluationDto> list = mutualEvaluationService.getmutualEvaluationListByStudentId(id);

        return Result.succ(list);
    }

    //获取互评作业详情
    @GetMapping("/evaluate")
    public Result getMutualEvaluationInfo(@RequestParam(value = "studentId",required=true) Integer studentId,@RequestParam(value = "templateId",required=true) Integer templateId){

        JSONObject json = mutualEvaluationService.getMutualEvaluationInfo(studentId, templateId);
        return Result.succ(json);
    }

    //提交评价
    @PostMapping("/submitEvaluation")
    public Result submitEvaluation(@RequestBody String str){
        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(str);
        String answerDtoListStr = jsonObject.getString("answerDtoList");
        System.out.println(str);
        List<AnswerDto> answerDtoList = com.alibaba.fastjson.JSONObject.parseArray(answerDtoListStr, AnswerDto.class);
        Integer templateId = jsonObject.getInteger("templateId");
        Integer answerId = jsonObject.getInteger("answerId");
        System.out.println(answerDtoList);

        if (mutualEvaluationService.submitEvaluation(templateId,answerId,answerDtoList)){
            return Result.succ(true);
        }else {
            return Result.fail("提交失败");
        }
    }

}
