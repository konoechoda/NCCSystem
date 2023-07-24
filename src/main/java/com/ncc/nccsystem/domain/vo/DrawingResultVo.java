package com.ncc.nccsystem.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrawingResultVo {

    //辅导员名称
    private String counselorName;
    //学校名称
    private String schoolName;
    //抽签结果
    private String drawNumber;
}
