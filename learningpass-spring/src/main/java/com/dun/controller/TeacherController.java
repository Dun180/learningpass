package com.dun.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dun.common.lang.Result;
import com.dun.entity.CClass;
import com.dun.entity.Task;
import com.dun.service.ClassService;
import com.dun.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/classes/{id}")
    public Result GetClassesByTeacherId(@PathVariable("id") Integer id,@RequestParam(defaultValue = "1") Integer currentPage) {

        //List<CClass> cClasses = classService.getClassListByTeacherId(id);
        //分页

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

    @PostMapping("/class:")
    public Result createClass(@RequestBody Map<String,Object> map){
        System.out.println(map);
        CClass cClass = new CClass();
        cClass.setSemester(map.get("semester").toString());
        String name = map.get("name").toString();
        //班级名查重
        if(classService.getOne(new QueryWrapper<CClass>().eq("name",name))==null){
            cClass.setName(name);
        }else{
            return Result.fail("班级名重复");
        }

        cClass.setTeacherId(Integer.parseInt(map.get("teacherId").toString()));
        String code = RandomUtil.randomStringUpper(6);

        //班级代码查重
        while(classService.getOne(new QueryWrapper<CClass>().eq("code",code))!=null){
            code = RandomUtil.randomStringUpper(6);
        }
        cClass.setCode(code);

        cClass.setCreateTime(new Date());
        cClass.setUpdateTime(new Date());
        if (classService.addClass(cClass)){
            return Result.succ(MapUtil.builder()
                    .put("addResult",true)
                    .map()
            );
        }else {
            return Result.fail("添加失败");
        }

    }

    //没测过
    @PostMapping("/task:")
    public Result createTask(@RequestBody Map<String,Object> map){
        System.out.println(map);
        Task task = new Task();
        task.setTitle(map.get("title").toString());
        task.setCreatorId(Integer.parseInt(map.get("creatorId").toString()));
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());
        if (taskService.addTask(task)){
            return Result.succ(MapUtil.builder()
                    .put("addResult",true)
                    .map()
            );
        }else {
            return Result.fail("添加失败");
        }

    }

}
