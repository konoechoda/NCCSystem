package com.ncc.nccsystem.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ncc.nccsystem.domain.entity.User;
import com.ncc.nccsystem.domain.vo.ScoresVo;


/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2023-07-12 17:19:32
 */
public interface UserService extends IService<User> {
    SaResult doLogin(User user);

    SaResult doRegister(User user);

    SaResult logout();

    SaResult processJudge(ScoresVo scoresVo, String type, Integer id);

    SaResult judge(ScoresVo scoresVo, String type);

    SaResult writtenJudge(ScoresVo scoresVo);
}
