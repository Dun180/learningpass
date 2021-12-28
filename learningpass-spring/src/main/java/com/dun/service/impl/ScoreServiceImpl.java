package com.dun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dun.entity.Score;
import com.dun.mapper.ScoreMapper;
import com.dun.service.ScoreService;
import org.springframework.stereotype.Service;

@Service("scoreService")
public class ScoreServiceImpl extends ServiceImpl<ScoreMapper, Score> implements ScoreService {
}
