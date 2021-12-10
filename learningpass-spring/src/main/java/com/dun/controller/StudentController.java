package com.dun.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dun.common.lang.Result;
import com.dun.entity.CClass;
import com.dun.entity.ClassStudentRel;
import com.dun.service.ClassService;
import com.dun.service.ClassStudentRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    ClassService classService;

    @Autowired
    ClassStudentRelService csrService;

    //获取班级列表
    @GetMapping("/classes/{id}")
    public Result GetClassesByTeacherId(@PathVariable("id") Integer id,@RequestParam(defaultValue = "1") Integer currentPage) {

        //List<CClass> cClasses = classService.getClassListByTeacherId(id);
        //分页

        System.out.println("id:"+id+" page:"+currentPage);
        if(currentPage == null || currentPage < 1) currentPage = 1;
        Page page = new Page<>(currentPage,6);
        IPage pageData = classService.page(page, new QueryWrapper<CClass>()
                .inSql("id","select class_id as id from class_student_rel where student_id ='"+id+"'")
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
}
