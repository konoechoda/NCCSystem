package com.ncc.nccsystem.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ncc.nccsystem.domain.entity.SecondRound;


/**
 * (SecondRound)表服务接口
 *
 * @author makejava
 * @since 2023-07-15 15:10:53
 */
public interface SecondRoundService extends IService<SecondRound> {

    SaResult secondRoundDraw(String groupName);

    SaResult interviewRound(String groupName);

}
