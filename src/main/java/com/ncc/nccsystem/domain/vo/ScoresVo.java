package com.ncc.nccsystem.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoresVo {
    //选手姓名
    private String counselorName;

    //参赛组别
    private String grouping;

    //身份证号码
    private String idCard;

    //签号
    private String drawNumber;

    //分数
    private Integer Score;
}
