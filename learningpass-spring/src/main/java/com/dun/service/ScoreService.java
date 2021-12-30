package com.dun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dun.common.dto.ScoreDto;
import com.dun.entity.Score;

import java.util.List;

public interface ScoreService extends IService<Score> {

    //查询成绩
    public List<ScoreDto> scoreQuery(Integer studentId,Integer select);
}
