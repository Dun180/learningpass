package com.dun.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
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

    //根据班级id修改班级信息
    @PostMapping("/class/modify")
    public Result modifyClassInfo(@RequestBody Map<String,Object> map){
        System.out.println(map);
        Integer id = Integer.parseInt(map.get("id").toString());
        CClass cClass = classService.getOne(new QueryWrapper<CClass>().eq("id", id));
        cClass.setSemester(map.get("semester").toString());
        String name = map.get("name").toString();
        //班级名查重
        if(classService.getOne(new QueryWrapper<CClass>().eq("name",name).ne("id",id))==null){
            cClass.setName(name);
        }else{
            return Result.fail("班级名重复");
        }


        if (classService.updateById(cClass)){
            return Result.succ(true);
        }else {
            return Result.fail("修改失败");
        }
    }

    //向班级添加（创建）学生
    @PostMapping("/class/add/student:")
    public Result addStudentToClass(@RequestBody Map<String,Object> map){
        String username = map.get("username").toString();
        User user = new User();

        //用户名（学号）查重
        if(userService.getOne(new QueryWrapper<User>().eq("username",username))==null){
            user.setUsername(map.get("username").toString());
        }else {
            return Result.fail("学号重复");
        }
        user.setName(map.get("name").toString());
        user.setPassword(SecureUtil.md5(map.get("username").toString()));
        user.setIdentity("Student");
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());

        if (userService.save(user)){
            ClassStudentRel csr = new ClassStudentRel();
            User mUser = userService.getOne(new QueryWrapper<User>().eq("username", username));
            csr.setStudentId(mUser.getId());
            csr.setClassId(Integer.parseInt(map.get("classId").toString()));
            if (csrService.save(csr)){
                return Result.succ(true);
            }else {
                return Result.fail("加入班级失败");
            }
        }else {
            return Result.fail("添加失败");
        }
    }
}
