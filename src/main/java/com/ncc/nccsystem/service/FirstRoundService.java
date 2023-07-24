package com.ncc.nccsystem.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ncc.nccsystem.domain.entity.FirstRound;
import org.springframework.web.multipart.MultipartFile;



/**
 * (FirstRound)表服务接口
 *
 * @author makejava
 * @since 2023-07-13 17:17:03
 */
public interface FirstRoundService extends IService<FirstRound> {

    SaResult randomGroup(String groupName);

    SaResult importScore(MultipartFile excelFile);
}
