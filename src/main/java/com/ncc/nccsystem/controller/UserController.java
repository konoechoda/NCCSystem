package com.ncc.nccsystem.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.util.SaResult;
import com.alibaba.fastjson.JSON;
import com.ncc.nccsystem.domain.entity.User;
import com.ncc.nccsystem.domain.vo.ScoresVo;
import com.ncc.nccsystem.enums.AppHttpCodeEnum;
import com.ncc.nccsystem.handler.exception.SystemException;
import com.ncc.nccsystem.service.UserService;
import com.rabbitmq.client.Channel;
import io.swagger.annotations.*;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@Api(tags = "用户接口")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param user 用户名 密码
     * @return cn.dev33.satoken.util.SaResult
     * 访问地址：http://localhost:8082/user/doLogin
     */
    @SaIgnore
    @ApiOperation(value = "登录接口")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功", response = String.class),
            @ApiResponse(code = 500, message = "msg"),
            @ApiResponse(code = 500, message = "账号或密码错误"),
            @ApiResponse(code = 502, message = "必需填写用户名")
    })
    @GetMapping("/doLogin")
    public SaResult doLogin(@ApiParam(value = "用户", required = true) @RequestBody User user){
        if(!StringUtils.hasText(user.getAccount())){
            //提示 用户名非法 需填写用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return userService.doLogin(user);
    }

    /**
     * 用户注册
     * @param user 用户名 密码
     * @return cn.dev33.satoken.util.SaResult
     * 访问地址：http://localhost:8082/user/doRegister
     */
    @SaCheckRole("admin")
    @ApiOperation(value = "注册接口")
    @ApiResponses({
            @ApiResponse(code = 200, message = "注册成功"),
            @ApiResponse(code = 500, message = "msg"),
            @ApiResponse(code = 500, message = "登录异常"),
            @ApiResponse(code = 500, message = "缺少角色权限"),
            @ApiResponse(code = 500, message = "账号已存在"),
            @ApiResponse(code = 502, message = "必需填写用户名")
    })
    @PostMapping("/doRegister")
    public SaResult doRegister(@ApiParam(value = "用户", required = true) @RequestBody User user){
        if(!StringUtils.hasText(user.getAccount())){
            //提示 用户名非法 需填写用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return userService.doRegister(user);
    }

    /**
     * 用户注销
     * @return cn.dev33.satoken.util.SaResult
     * 访问地址：http://localhost:8082/user/logout
     */
    @SaCheckLogin
    @ApiOperation(value = "注销接口")
    @ApiResponses({
            @ApiResponse(code = 200, message = "注销成功"),
            @ApiResponse(code = 500, message = "登录异常"),
    })
    @GetMapping("/logout")
    public SaResult logout(){
        return userService.logout();
    }

    /**
     * 笔试评分
     * @param scoresVo 评分信息
     * 访问地址：http://localhost:8082/user/writtenJudge
     */
    @SaCheckRole("Judge")
    @ApiOperation(value = "笔试评分接口")
    @ApiResponses({
            @ApiResponse(code = 200, message = "评分成功"),
            @ApiResponse(code = 500, message = "msg"),
            @ApiResponse(code = 500, message = "评分为空"),
            @ApiResponse(code = 500, message = "登录异常"),
            @ApiResponse(code = 500, message = "缺少角色权限"),
            @ApiResponse(code = 500, message = "无报名信息")
    })
    @PostMapping("/writtenJudge")
    public SaResult writtenJudge(@ApiParam(value = "评分对象", required = true)@RequestBody ScoresVo scoresVo){
        return userService.writtenJudge(scoresVo);
    }

    /**
     * 评分
     * @param scoresVo 评分信息
     * @param type 评分类型
     * @return cn.dev33.satoken.util.SaResult
     * 访问地址：http://localhost:8082/user/updatePassword
     */
    @SaCheckRole("Judge")
    @ApiOperation(value = "评分接口")
    @ApiResponses({
            @ApiResponse(code = 200, message = "评分成功"),
            @ApiResponse(code = 500, message = "msg"),
            @ApiResponse(code = 500, message = "评分为空"),
            @ApiResponse(code = 500, message = "您已经完成该辅导员评分"),
            @ApiResponse(code = 500, message = "缺少角色权限"),
            @ApiResponse(code = 500, message = "登录异常"),
            @ApiResponse(code = 500, message = "无报名信息")
    })
    @PostMapping("/judge")
    public SaResult judge(@ApiParam(value = "评分对象", required = true) @RequestBody ScoresVo scoresVo,@ApiParam(value = "评分类型(discussion_score,interview_score)", required = true) String type){
        return userService.judge(scoresVo, type);
    }

    /**
     * 评分消息接收
     * @param map
     * @param channel
     * @param message
     * @return cn.dev33.satoken.util.SaResult
     * 访问地址：http://localhost:8082/user/updatePassword
     */
    @RabbitHandler
    @RabbitListener(queues = "NCCSystemDirectJudgeQueue")
    public void processJudge(Map map , Channel channel, Message message) throws IOException{
        String type = (String) map.get("type");
        Integer id = (Integer) map.get("id");
        ScoresVo scoresVo = JSON.parseObject(JSON.toJSONString(map.get("ScoresVo")), ScoresVo.class);
        userService.processJudge(scoresVo,type,id);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
