package com.dun.controller;

import cn.hutool.core.map.MapUtil;
import com.dun.common.lang.Result;
import com.dun.entity.CClass;
import com.dun.service.ClassService;
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

    @GetMapping("/classes/{id}")
    public Result GetClassesByTeacherId(@PathVariable("id") Integer id) {

        List<CClass> cClasses = classService.getClassListByTeacherId(id);

        return Result.succ(MapUtil.builder()
                .put("classes", cClasses)
                .map()
        );
    }

    @PostMapping("/class:")
    public Result createClass(@RequestBody Map<String,Object> map){
        System.out.println(map);
        CClass cClass = new CClass();
        cClass.setSemester(map.get("semester").toString());
        cClass.setName(map.get("name").toString());
        cClass.setTeacherId(Integer.parseInt(map.get("teacherId").toString()));
        cClass.setCreateTime(new Date());
        cClass.setUpdateTime(new Date());
        if (classService.addClass(cClass)){
            return Result.succ(MapUtil.builder()
                    .put("addClassResult",true)
                    .map()
            );
        }else {
            return Result.fail("添加失败");
        }

    }

}
