package com.dun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dun.entity.Group;

public interface GroupService extends IService<Group> {
    public boolean createGroup(Integer classId,String groupName,Integer groupMode,Integer max,Integer min);

    public boolean addGroupMember(Integer groupId,Integer studentId);

    public boolean deleteGroupMember(Integer groupId,Integer studentId);
}
