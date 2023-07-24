package com.ncc.nccsystem.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.util.SaResult;
import com.ncc.nccsystem.service.ScoresService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@Api(tags = "最终结果")
@RequestMapping("/scores")
public class ScoresController {

    @Autowired
    private ScoresService scoresService;

    /**
     * 导出成绩表(.pdf)
     * @param groupName 分组名称
     * @param response 响应
     * @return SaResult
     * 访问地址：http://localhost:8082/scores/exportPdf
     */
    @SaCheckRole("host")
    @ApiOperation("导出成绩表(.pdf)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok"),
            @ApiResponse(code = 500, message = "msg"),
            @ApiResponse(code = 500, message = "登录异常"),
            @ApiResponse(code = 500, message = "缺少角色权限"),
            @ApiResponse(code = 500, message = "分组名称错误")
    })
    @GetMapping( "/exportPdf")
    public SaResult exportPdf(@ApiParam(value = "分组名,example = 本科组:BK,高职组:GZ", required = true)String groupName, HttpServletResponse response) {
        return scoresService.exportPdf(groupName,response);
    }
}
