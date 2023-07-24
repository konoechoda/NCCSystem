package com.ncc.nccsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ncc.nccsystem.domain.entity.JudgeScores;

import java.util.List;


/**
 * (JudgeScores)表数据库访问层
 *
 * @author makejava
 * @since 2023-07-19 20:02:53
 */
public interface JudgeScoresMapper extends BaseMapper<JudgeScores> {

    List<JudgeScores> calculateFinalScore();
}
