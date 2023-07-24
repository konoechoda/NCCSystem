package com.ncc.nccsystem.domain.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CounselorsVo {
    //学校名
    private String schoolName;
    //姓名
    private String name;
    //身份证号码
    private String idCard;
    //参赛组别
    private String grouping;
    //报名时间
    private Date registrationTime;
}
