package com.ncc.nccsystem.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.util.SaResult;
import com.ncc.nccsystem.domain.vo.DrawingResultVo;
import com.ncc.nccsystem.service.FirstRoundService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(tags = "第一轮比赛")
@RequestMapping("/firstRound")
public class FirstRoundController {

    @Autowired
    private FirstRoundService firstRoundService;

    /**
     * 随机分组
     * @param groupName 分组名称
     * @return cn.dev33.satoken.util.SaResult
     * 访问地址：http://localhost:8082/firstRound/randomGroup
     */
    @SaCheckRole("host")
    @ApiOperation(value = "第一轮随机分组")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok",response = DrawingResultVo.class),
            @ApiResponse(code = 500, message = "msg"),
            @ApiResponse(code = 500, message = "登录异常"),
            @ApiResponse(code = 500, message = "缺少角色权限"),
            @ApiResponse(code = 500, message = "分组名称错误")
    })
    @GetMapping("/randomGroup")
    public SaResult randomGroup(@ApiParam(value = "分组名,example = 本科组:BK,高职组:GZ", required = true)String groupName) {
        return firstRoundService.randomGroup(groupName);
    }

    /**
     * 笔试成绩excel表格一键导入
     * @param excelFile excel文件
     * @return cn.dev33.satoken.util.SaResult
     * 访问地址：http://localhost:8082/firstRound/importScore
     */
    @SaCheckRole("host")
    @ApiOperation(value = "第一轮成绩导入(excel)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "导入成功"),
            @ApiResponse(code = 500, message = "msg"),
            @ApiResponse(code = 500, message = "文件读取失败"),
            @ApiResponse(code = 500, message = "登录异常"),
            @ApiResponse(code = 500, message = "缺少角色权限"),
            @ApiResponse(code = 503, message = "信息出现异常,请检查参赛人员的个人信息是否正确")
    })
    @PostMapping("/importScore")
    public SaResult importScore(@ApiParam(value = "笔试成绩excel表格", required = true)MultipartFile excelFile) {
         return firstRoundService.importScore(excelFile);
    }
}