package com.dun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dun.entity.*;
import com.dun.mapper.*;
import com.dun.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("groupService")
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private GroupStudentRelMapper gsrMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean createGroup(Integer classId, String groupName, Integer groupMode, Integer max, Integer min) {

        switch (groupMode){
            case 1://随机分组
            case 3://自由分组
            case 2://顺序分组
                //暂时没有考虑最小组员数量
                //获取班级全部成员
                List<User> memberList = userMapper.selectList(new QueryWrapper<User>().inSql("id", "select student_id as id from class_student_rel where class_id ='" + classId + "'"));
                //定义本次创建啊的分组数量
                Integer groupNum = memberList.size()/max + 1;
                for (int i = 0; i < groupNum; i++) {
                    if (memberList.size() == 0) break;
                    Group tempGroup = new Group();
                    tempGroup.setGroupName(groupName+"<"+(i+1)+">");
                    tempGroup.setClassId(classId);
                    tempGroup.setCreateTime(new Date());
                    tempGroup.setUpdateTime(new Date());
                    if(groupMapper.insert(tempGroup)==0){
                        return false;
                    }
                    //为分组中添加成员
                    for (int j = 0; j < max; j++) {
                        if(memberList.size()>0){
                            User tempUser = memberList.remove(0);
                            if (tempUser!=null){
                                GroupStudentRel tempGsr = new GroupStudentRel();
                                tempGsr.setGroupId(tempGroup.getId());
                                tempGsr.setStudentId(tempUser.getId());
                                if(gsrMapper.insert(tempGsr)==0) return false;
                            }
                        }else {
                            break;
                        }
                    }

                }
                break;
            default:
                break;
        }


        return true;
    }
}
