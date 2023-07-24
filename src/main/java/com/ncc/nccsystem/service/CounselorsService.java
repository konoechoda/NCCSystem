package com.ncc.nccsystem.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ncc.nccsystem.domain.entity.Counselors;

import java.util.List;

/**
 * (Counselors)表服务接口
 *
 * @author makejava
 * @since 2023-07-12 22:02:42
 */
public interface CounselorsService extends IService<Counselors> {

    SaResult register(Counselors counselors);

    SaResult queryByIdCard(String idCard);

    SaResult delete(String name, String idCard);

    SaResult queryByName(String name);

    List<Counselors> selectNumberOfCountByScore(int number, String groupName);
}
