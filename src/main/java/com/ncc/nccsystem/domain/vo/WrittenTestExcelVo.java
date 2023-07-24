package com.ncc.nccsystem.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WrittenTestExcelVo {

    //选手姓名
    @ExcelProperty(value = "选手")
    private String counselorName;

    //参赛组别
    @ExcelProperty(value = "参赛组别")
    private String grouping;

    //身份证号码
    @ExcelProperty(value = "身份证号码")
    private String idCard;

    //第一轮签号
    @ExcelProperty(value = "第一轮签号")
    private String drawNumber;

    //分数
    @ExcelProperty(value = "分数")
    private Integer writtenTestScore;
}
