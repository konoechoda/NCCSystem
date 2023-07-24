package com.ncc.nccsystem.service.impl;

import cn.dev33.satoken.stp.StpInterface;
import com.ncc.nccsystem.domain.entity.User;
import com.ncc.nccsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限验证接口扩展
 * 获取当前账号权限码
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private UserService userService;


    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return null; //未使用
    }

    /**
     * 返回一个账号所拥有的角色标识集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> list = new ArrayList<>();
        //根据loginId在数据库中查询用户信息
        User user = userService.getById(Integer.parseInt(loginId.toString()));
        //添加用户权限
        list.add(user.getRole());
        return list;
    }
}
