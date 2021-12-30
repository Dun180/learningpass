package com.dun.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dun.common.dto.MutualEvaluationCompletionDto;
import com.dun.common.dto.MutualEvaluationDto;
import com.dun.common.dto.TaskArrangementDto;
import com.dun.common.dto.TaskCompletionDto;
import com.dun.common.lang.Result;
import com.dun.entity.*;
import com.dun.service.*;
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

    @Autowired
    GroupService groupService;

    @Autowired
    TaskService taskService;

    @Autowired
    TaskArrangementService taService;

    @Autowired
    MutualEvaluationService mutualEvaluationService;

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
        Page page = new Page<>(currentPage,10);
        IPage pageData = userService.page(page, new QueryWrapper<User>()
                .inSql("id","select student_id as id from class_student_rel where class_id ='"+id+"'")
        );
        System.out.println(pageData.getRecords());

        return Result.succ(pageData);
    }

    //根据班级id获取班级成员
    @GetMapping("/class/memberList/{id}")
    public Result getClassMemberListById(@PathVariable("id") Integer id) {


        List<User> userList = userService.list(new QueryWrapper<User>()
                .inSql("id", "select student_id as id from class_student_rel where class_id ='" + id + "'")
        );


        return Result.succ(userList);
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

    //删除学生
    @PostMapping("/class/delete")
    public Result deleteStudent(@RequestBody Map<String,Object> map){

        if(csrService.removeByMap(map)){
            return Result.succ(true);

        }else {
            return Result.fail("删除失败");
        }
    }

    //查询学生
    @GetMapping("/class/select")
    public Result selectStudent(@RequestParam Integer classId,@RequestParam(required = false,defaultValue = "") String select,@RequestParam(required = false,defaultValue = "") String value){
        List<User> list = userService.list(new QueryWrapper<User>()
                .inSql("id", "select student_id as id from class_student_rel where class_id ='" + classId + "'")
                .eq(select, value)
        );


        return Result.succ(list);
    }

    //获取分组列表
    @GetMapping("/class/{id}/groupList")
    public Result getGroupList(@PathVariable("id") Integer id){

        List<Group> groupList = groupService.list(new QueryWrapper<Group>()
                .eq("class_id", id));

        return Result.succ(groupList);
    }

    //获取分组成员
    @GetMapping("/class/group/{id}")
    public Result getGroupMember(@PathVariable("id") Integer id){
        List<User> list = userService.list(new QueryWrapper<User>().inSql("id", "select student_id as id from group_student_rel where group_id=" + id));


        return Result.succ(list);
    }

    //根据班级id获取作业布置列表
    @GetMapping("/class/{id}/taskArrangementList")
    public Result getTaskArrangementList(@PathVariable("id") Integer id){

        List<TaskArrangementDto> dtoList = new ArrayList<>();

        List<TaskArrangement> taskArrangementList = taService.list(new QueryWrapper<TaskArrangement>().eq("class_id", id));

        for (int i = 0; i < taskArrangementList.size(); i++) {
            TaskArrangementDto taskArrangementDto = new TaskArrangementDto(taskArrangementList.get(i));
            String taskTitle = taskService.getOne(new QueryWrapper<Task>().eq("id",taskArrangementList.get(i).getTaskId())).getTitle();
            taskArrangementDto.setTaskTitle(taskTitle);
            dtoList.add(taskArrangementDto);
        }

        return Result.succ(dtoList);
    }

    //获取作业完成情况
    @GetMapping("/class/taskCompletion")
    public Result getTaskCompletion(@RequestParam(value = "taskArrangementId",required=true) Integer taskArrangementId){


        List<TaskCompletionDto> taskCompletion = taService.getTaskCompletion(taskArrangementId);

        return Result.succ(taskCompletion);
    }

    //根据班级id获取互评作业列表
    @GetMapping("/class/{id}/mutualEvaluationList")
    public Result getMutualEvaluationList(@PathVariable("id") Integer id){

        List<MutualEvaluationDto> dtoList = mutualEvaluationService.getMutualEvaluationList(id);

        return Result.succ(dtoList);
    }

    //获取互评作业完成情况
    @GetMapping("/class/mutualEvaluationCompletion")
    public Result getMutualEvaluationCompletion(@RequestParam(value = "templateId",required=true) Integer templateId){


        List<MutualEvaluationCompletionDto> dtoList = mutualEvaluationService.getMutualEvaluationCompletion(templateId);

        return Result.succ(dtoList);
    }

    //添加分组成员
    @PostMapping("/class/group/add")
    public Result addGroupMember(@RequestBody Map<String,Object> map){

        Integer studentId = Integer.parseInt(map.get("studentId").toString());
        Integer groupId = Integer.parseInt(map.get("groupId").toString());
        System.out.println("studentId:"+studentId+"  groupId:"+groupId);
        if(groupService.addGroupMember(groupId,studentId)){
            return Result.succ(true);
        }else {
            return Result.fail("添加失败");
        }
    }

    //删除分组成员
    @PostMapping("/class/group/delete")
    public Result deleteGroupMember(@RequestBody Map<String,Object> map){

        Integer studentId = Integer.parseInt(map.get("studentId").toString());
        Integer groupId = Integer.parseInt(map.get("groupId").toString());

        if(groupService.deleteGroupMember(groupId,studentId)){
            return Result.succ(true);
        }else {
            return Result.fail("删除失败");
        }
    }
}
