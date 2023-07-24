package com.ncc.nccsystem.domain.entity;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Counselors)表实体类
 *
 * @author makejava
 * @since 2023-07-12 22:02:42
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("counselors")
public class Counselors  {
    //主键
    @TableId
    private Long id;

    //学校名
    private Long schoolId;
    //姓名
    private String name;
    //身份证号码
    private String idCard;
    //参赛组别
    private String grouping;
    //报名时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date registrationTime;


}

