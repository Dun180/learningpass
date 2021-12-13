package com.dun.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dun.common.lang.Result;
import com.dun.entity.CClass;
import com.dun.entity.ClassStudentRel;
import com.dun.entity.User;
import com.dun.service.ClassService;
import com.dun.service.ClassStudentRelService;
import com.dun.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class ClassController {
    @Autowired
    UserService userService;

    @Autowired
    ClassService classService;

    @Autowired
    ClassStudentRelService csrService;

    //根据班级id获取班级
    @GetMapping("/class/{id}")
    public Result getClassById(@PathVariable("id") Integer id) {

        CClass cClass = classService.getOne(new QueryWrapper<CClass>().eq("id", id));

        return Result.succ(cClass);
    }

    //根据班级id获取班级成员
    @GetMapping("/class/member/{id}")
    public Result getClassMemberById(@PathVariable("id") Integer id,@RequestParam(defaultValue = "1") Integer currentPage) {

//        List<ClassStudentRel> csrList = csrService.list(new QueryWrapper<ClassStudentRel>().eq("class_id", id));
//
//        List<User> users = new ArrayList<User>();
//        for (ClassStudentRel csr:csrList
//             ) {
//            User user = userService.getOne(new QueryWrapper<User>().eq("id", csr.getStudentId()));
//            users.add(user);
//
//        }
        System.out.println("----------------------");
        Page page = new Page<>(currentPage,15);
        IPage pageData = userService.page(page, new QueryWrapper<User>()
                .inSql("id","select student_id as id from class_student_rel where class_id ='"+id+"'")
        );
        System.out.println(pageData.getRecords());

        return Result.succ(pageData);
    }

    //添加班级
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
}