package com.ncc.nccsystem.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ncc.nccsystem.domain.entity.Scores;

import javax.servlet.http.HttpServletResponse;


/**
 * (Scores)表服务接口
 *
 * @author makejava
 * @since 2023-07-14 21:24:12
 */
public interface ScoresService extends IService<Scores> {

    SaResult exportPdf(String groupName,HttpServletResponse response);
}
