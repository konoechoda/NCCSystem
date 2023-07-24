package com.ncc.nccsystem.domain.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (JudgeScores)表实体类
 *
 * @author makejava
 * @since 2023-07-19 20:02:54
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("judge_scores")
public class JudgeScores  {
    //主键
    @TableId
    private Integer id;

    //评委id
    private Integer userId;
    
    private String idCard;
    //案例研讨成绩
    private Integer discussionScore;
    //谈心谈话成绩
    private Integer interviewScore;



}

