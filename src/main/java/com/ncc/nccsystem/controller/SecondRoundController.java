package com.ncc.nccsystem.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.util.SaResult;
import com.ncc.nccsystem.domain.vo.SecondRoundVo;
import com.ncc.nccsystem.service.SecondRoundService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "第二轮比赛")
@RequestMapping("/secondRound")
public class SecondRoundController {

    @Autowired
    private SecondRoundService secondRoundService;

    /**
     *  二轮抽签
     * @param groupName 分组名称
     * @return cn.dev33.satoken.util.SaResult
     * 访问地址：http://localhost:8082/secondRound/secondRoundDraw
     */
    @SaCheckRole("host")
    @ApiOperation(value = "二轮抽签")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok",response = SecondRoundVo.class),
            @ApiResponse(code = 500, message = "msg"),
            @ApiResponse(code = 500, message = "登录异常"),
            @ApiResponse(code = 500, message = "缺少角色权限"),
            @ApiResponse(code = 500, message = "分组名称错误")
    })
    @PostMapping("/secondRoundDraw")
    public SaResult secondRoundDraw(@ApiParam(value = "分组名,example = 本科组:BK,高职组:GZ", required = true)String groupName){
        return secondRoundService.secondRoundDraw(groupName);
    }

    /**
     *  谈心谈话环节
     * @param groupName 分组名称
     * @return cn.dev33.satoken.util.SaResult
     * 访问地址：http://localhost:8082/secondRound/discussionRound
     */
    @SaCheckRole("host")
    @ApiOperation(value = "谈心谈话环节")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok",response = SecondRoundVo.class),
            @ApiResponse(code = 500, message = "msg"),
            @ApiResponse(code = 500, message = "登录异常"),
            @ApiResponse(code = 500, message = "缺少角色权限"),
            @ApiResponse(code = 500, message = "分组名称错误")
    })
    @GetMapping("/discussionRound")
    public SaResult interviewRound(@ApiParam(value = "分组名,example = 本科组:BK,高职组:GZ", required = true)String groupName){
        return secondRoundService.interviewRound(groupName);
    }
}
