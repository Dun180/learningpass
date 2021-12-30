package com.dun.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dun.common.dto.AnswerDto;
import com.dun.common.dto.TaskCompletionDto;
import com.dun.common.lang.Result;
import com.dun.entity.*;
import com.dun.service.*;
import com.dun.util.DunUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    ClassService classService;

    @Autowired
    TaskService taskService;

    @Autowired
    QuestionService questionService;

    @Autowired
    GroupService groupService;

    @Autowired
    TaskArrangementService taService;

    @Autowired
    AnswerService answerService;

    @Autowired
    MutualEvaluationService mutualEvaluationService;

    //根据教师id获取班级列表
    @GetMapping("/classes/{id}")
    public Result getClassesByTeacherId(@PathVariable("id") Integer id,@RequestParam(defaultValue = "1") Integer currentPage) {


        System.out.println("id:"+id+" page:"+currentPage);
        if(currentPage == null || currentPage < 1) currentPage = 1;
        Page page = new Page<>(currentPage,6);
        IPage pageData = classService.page(page, new QueryWrapper<CClass>()
                .orderByDesc("create_time")
                .eq("teacher_id",id)
        );
        System.out.println(pageData.getRecords());
        return Result.succ(pageData);
    }

    //根据教师id获取作业列表
    @GetMapping("/{id}/taskList")
    public Result getTasksByTeacherId(@PathVariable("id") Integer id,@RequestParam(defaultValue = "1") Integer currentPage){
        System.out.println("id:"+id+" page:"+currentPage);
        if(currentPage == null || currentPage < 1) currentPage = 1;
        Page page = new Page<>(currentPage,6);
        IPage pageData = taskService.page(page, new QueryWrapper<Task>()
                .eq("creator_id", id)
                .orderByDesc("create_time")
        );
        System.out.println(pageData.getRecords());
        return Result.succ(pageData);
    }

    //根据教师id获取全部作业
    @GetMapping("/{id}/allTask")
    public Result getAllTaskByTeacherId(@PathVariable("id") Integer id){
        System.out.println("id="+id);
        List<Task> list = taskService.list(new QueryWrapper<Task>()
                .eq("creator_id", id)
                .orderByDesc("create_time"));
        return Result.succ(list);

    }

    //创建或修改作业
    @PostMapping("/task:")
    public Result createOrUpdateTask(@RequestBody Map<String,Object> map){
        System.out.println(map);
        //创建list用来记录该作业对应的问题id  在最后将不在该list中的问题删除
        List<Integer> questionIdList = new ArrayList<>();

        Task task = new Task();
        if(map.get("taskId")!=null&&map.get("taskId").toString()!=""){
            //如果作业存在 直接设置作业id，不需要自动递增，否则就设置创建时间并且id自动递增
            task.setId(Integer.parseInt(map.get("taskId").toString()));
        }else{
            task.setCreateTime(new Date());
        }
        task.setUpdateTime(new Date());
        task.setTitle(map.get("title").toString());
        task.setCreatorId(Integer.parseInt(map.get("creatorId").toString()));
        //插入或更新作业
        if (taskService.saveOrUpdate(task)){
            Question question = new Question();
            if(map.get("questionId")!=null&&map.get("questionId").toString()!=""){
                //如果问题存在 直接设置问题id，不需要自动递增
                question.setId(Integer.parseInt(map.get("questionId").toString()));
            }
            question.setTaskId(task.getId());
            question.setScore(Integer.parseInt(map.get("score").toString()));
            question.setStem(map.get("stem").toString());
            //插入或更新问题
            if (questionService.saveOrUpdate(question)){
                questionIdList.add(question.getId());
                JSONArray dynamicItem = JSONUtil.parseArray(map.get("dynamicItem"));
                if(dynamicItem.size()>0){
                    for (int i = 0; i < dynamicItem.size(); i++) {
                        Question tempQuestion = new Question();
                        if(dynamicItem.getByPath("["+i+"].id")!=null&&dynamicItem.getByPath("["+i+"].id")!=""){
                            //如果问题存在 直接设置问题id，不需要自动递增，并将该id加入list表中防止删除
                            tempQuestion.setId(Integer.parseInt(dynamicItem.getByPath("["+i+"].id").toString()));
                            questionIdList.add(tempQuestion.getId());
                        }
                        tempQuestion.setTaskId(task.getId());
                        tempQuestion.setScore(Integer.parseInt(dynamicItem.getByPath("["+i+"].score").toString()));
                        tempQuestion.setStem(dynamicItem.getByPath("["+i+"].stem").toString());
                        //插入或更新问题
                        if (questionService.saveOrUpdate(tempQuestion)){
                            questionIdList.add(tempQuestion.getId());
                        }else {
                            return Result.fail("问题更新失败");
                        }
                    }

                }
                if (map.get("taskId")!=null){
                    //如果作业存在，就将除了list中记录的问题全删除
                    System.out.println(questionIdList);
                    boolean remove = questionService.remove(new QueryWrapper<Question>()
                            .eq("task_id", task.getId())
                            .notIn("id", questionIdList)
                    );

                }
                return Result.succ(true);
            }else {
                return Result.fail("问题更新失败");
            }

        }else {
            return Result.fail("作业更新失败");
        }

    }

    //获取作业
    @GetMapping("/tasks/{id}")
    public Result getTask(@PathVariable("id") Integer id){
        Task task = taskService.getOne(new QueryWrapper<Task>().eq("id", id));
        List<Question> questionList = questionService.list(new QueryWrapper<Question>().eq("task_id", task.getId()));
        Question question1 = questionList.get(0);
        questionList.remove(0);
        JSONObject json = JSONUtil.createObj()
                .set("taskId",task.getId())
                .set("creatorId",task.getCreatorId())
                .set("title",task.getTitle())
                .set("questionId",question1.getId())
                .set("score",question1.getScore())
                .set("stem",question1.getStem())
                .set("dynamicItem",questionList)
                ;
        System.out.println(json);

        return Result.succ(json);
    }

    //创建分组
    @PostMapping("/group:")
    public Result creatGroup(@RequestBody Map<String,Object> map){
        System.out.println("creatGroup:");
        System.out.println(map);

        boolean flag = groupService.createGroup(
                Integer.parseInt(map.get("classId").toString()),
                map.get("groupName").toString(),
                Integer.parseInt(map.get("groupMode").toString()),
                Integer.parseInt(map.get("maximumNumber").toString()),
                Integer.parseInt(map.get("minimumNumber").toString())
        );

        if (flag){
            return Result.succ(true);
        }
        else{
            return Result.fail("创建分组错误");
        }
    }

    //布置作业
    @PostMapping("/taskArrangement")
    public Result taskArrangement(@RequestBody Map<String,Object> map) throws ParseException {
        System.out.println("----------------");
        System.out.println("taskArrangement");
        System.out.println(map);
        System.out.println("----------------");
        Object timeObj = map.get("time");
        List<String> timeList = DunUtils.objToList(timeObj,String.class);
        System.out.println(timeList);
        Integer classId = Integer.parseInt(map.get("classId").toString());
        Integer taskId = Integer.parseInt(map.get("taskId").toString());
        Integer mode = Integer.parseInt(map.get("mode").toString());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");//注意月份是MM
        Date beginTime = simpleDateFormat.parse(timeList.get(0));
        Date endTime = simpleDateFormat.parse(timeList.get(1));

        System.out.println("taskId:"+taskId+" mode:"+mode+" beginTime:"+beginTime+" endTime:"+endTime);

        if(taService.taskArrangement(classId,taskId,mode,beginTime,endTime)){
            return Result.succ(true);
        }

        return Result.fail("布置失败");
    }

    //提交评分
    @PostMapping("/submitGrade")
    public Result submitGrade(@RequestBody String str){


        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(str);
        String answerDtoListStr = jsonObject.getString("answerDtoList");

        System.out.println(answerDtoListStr);
        List<AnswerDto> answerDtoList = com.alibaba.fastjson.JSONObject.parseArray(answerDtoListStr, AnswerDto.class);
        Integer answerId = jsonObject.getInteger("answerId");

        if (answerService.submitGrade(answerId,answerDtoList)){
            return Result.succ(true);
        }else {
            return Result.fail("提交失败");
        }
    }

    //创建互评作业
    @PostMapping("/createMutualEvaluationTask")
    public Result createMutualEvaluationTask(@RequestBody Map<String,Object> map) {

        try{
            Integer arrangementId = Integer.parseInt(map.get("arrangementId").toString());
            Integer gradeMode = Integer.parseInt(map.get("gradeMode").toString());
            Integer scoreDistribution = Integer.parseInt(map.get("scoreDistribution").toString());
            System.out.println(map);

            Object timeObj = map.get("time");
            List<String> timeList = DunUtils.objToList(timeObj,String.class);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");//注意月份是MM
            Date beginTime = simpleDateFormat.parse(timeList.get(0));
            Date endTime = simpleDateFormat.parse(timeList.get(1));

            if (mutualEvaluationService.createMutualEvaluationTask(arrangementId,gradeMode,scoreDistribution,beginTime,endTime)){
                return Result.succ(true);
            }else {
                return Result.fail("创建失败");
            }
        }catch (Exception e){
            return Result.fail(e.toString());

        }


    }

    //提交互评评价质量
    @PostMapping("/submitGradeEvaluate")
    public Result submitGradeEvaluate(@RequestBody Map<String,Object> map){
        Integer templateId = Integer.parseInt(map.get("templateId").toString());
        Integer userId = Integer.parseInt(map.get("userId").toString());
        Integer evaluationQuality = Integer.parseInt(map.get("evaluationQuality").toString());

        MutualEvaluation mutualEvaluation = mutualEvaluationService.getOne(new QueryWrapper<MutualEvaluation>()
                .eq("evaluator_id", userId)
                .eq("template_id", templateId)
        );
        mutualEvaluation.setState(2);
        mutualEvaluation.setEvaluationQuality(evaluationQuality);
        if (mutualEvaluationService.updateById(mutualEvaluation)){
            return Result.succ(true);
        }else {
            return Result.fail("提交失败");
        }
    }


}
