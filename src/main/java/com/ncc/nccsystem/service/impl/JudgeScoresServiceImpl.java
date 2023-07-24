package com.ncc.nccsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncc.nccsystem.domain.entity.JudgeScores;
import com.ncc.nccsystem.domain.entity.Scores;
import com.ncc.nccsystem.mapper.JudgeScoresMapper;
import com.ncc.nccsystem.service.JudgeScoresService;
import com.ncc.nccsystem.service.ScoresService;
import com.ncc.nccsystem.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * (JudgeScores)表服务实现类
 *
 * @author makejava
 * @since 2023-07-19 20:02:55
 */
@Service("judgeScoresService")
public class JudgeScoresServiceImpl extends ServiceImpl<JudgeScoresMapper, JudgeScores> implements JudgeScoresService {

    @Override
    public List<Scores> calculateFinalScore() {
        //计算最终得分
        List<JudgeScores> judgeScoresList = baseMapper.calculateFinalScore();

        return BeanCopyUtils.copyBeanList(judgeScoresList, Scores.class);
    }
}
