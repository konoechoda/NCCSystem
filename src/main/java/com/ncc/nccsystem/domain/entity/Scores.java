package com.ncc.nccsystem.domain.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Scores)表实体类
 *
 * @author makejava
 * @since 2023-07-14 21:24:12
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("scores")
public class Scores  {
    //主键
    @TableId
    private Long id;
    //辅导员名称
    private String counselorName;
    //身份证号
    private String idCard;
    //分组
    private String grouping;
    //第一轮签号
    private String drawNumber;
    //笔试成绩
    private Integer writtenTestScore;
    //案例研讨成绩
    private Integer discussionScore;
    //谈心谈话成绩
    private Integer interviewScore;
    //最终成绩
    private double finalScore;



}

