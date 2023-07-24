package com.ncc.nccsystem.domain.vo;


import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class FinalScoresVo {
    //辅导员名称
    @ExcelProperty(value = "选手")
    private String counselorName;
    //分组
    @ExcelProperty(value = "参赛组别")
    private String grouping;
    //第一轮签号
    @ExcelProperty(value = "第一轮签号")
    private String drawNumber;
    //笔试成绩
    @ExcelProperty(value = "笔试成绩(20%)")
    private Integer writtenTestScore;
    //案例研讨成绩
    @ExcelProperty(value = "案例研讨成绩(40%)")
    private Integer discussionScore;
    //谈心谈话成绩
    @ExcelProperty(value = "谈心谈话成绩(40%)")
    private Integer interviewScore;
    //最终成绩
    @ExcelProperty(value = "最终分数")
    private double finalScore;
}

