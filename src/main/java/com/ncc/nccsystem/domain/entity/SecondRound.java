package com.ncc.nccsystem.domain.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (SecondRound)表实体类
 *
 * @author makejava
 * @since 2023-07-15 15:10:53
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("second_round")
public class SecondRound  {
    //主键
    @TableId
    private Long id;

    //选手姓名
    private String name;

    //身份证号码
    private String idCard;

    //参赛组别
    private String grouping;

    //抽签结果
    private String drawNumber;



}