package com.ncc.nccsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ncc.nccsystem.domain.entity.JudgeScores;
import com.ncc.nccsystem.domain.entity.Scores;

import java.util.List;


/**
 * (JudgeScores)表服务接口
 *
 * @author makejava
 * @since 2023-07-19 20:02:54
 */
public interface JudgeScoresService extends IService<JudgeScores> {
    //计算最终得分
    public List<Scores> calculateFinalScore();
}
