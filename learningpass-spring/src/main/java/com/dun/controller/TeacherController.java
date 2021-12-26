package com.dun.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dun.common.lang.Result;
import com.dun.entity.CClass;
import com.dun.entity.Group;
import com.dun.entity.Question;
import com.dun.entity.Task;
import com.dun.service.ClassService;
import com.dun.service.GroupService;
import com.dun.service.QuestionService;
import com.dun.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        if(map.get("taskId")!=null){
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
            if(map.get("questionId")!=null){
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




}
