package com.ncc.nccsystem.domain.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (FirstRound)表实体类
 *
 * @author makejava
 * @since 2023-07-13 17:17:03
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("first_round")
public class FirstRound  {
    //主键
    @TableId
    private Long id;

    //辅导员id
    private Long counselorId;
    //抽签结果
    private String drawNumber;



}

