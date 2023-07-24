package com.ncc.nccsystem.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.util.SaResult;
import com.ncc.nccsystem.domain.entity.Counselors;
import com.ncc.nccsystem.domain.vo.CounselorsVo;
import com.ncc.nccsystem.service.CounselorsService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "辅导员报名接口")
@RequestMapping("/counselors")
public class CounselorsController {
    @Autowired
    private CounselorsService counselorsService;

    /**
     *  新增辅导员(报名)
     * @param counselors 辅导员实体类
     * @return cn.dev33.satoken.util.SaResult
     * 访问地址：http://localhost:8082/counselors/register
     */
    @SaCheckRole("school")
    @ApiOperation(value = "新增辅导员(报名)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "报名成功"),
            @ApiResponse(code = 500, message = "msg"),
            @ApiResponse(code = 500, message = "登录异常"),
            @ApiResponse(code = 500, message = "缺少角色权限"),
            @ApiResponse(code = 500, message = "msg")
    })
    @PostMapping("/register")
    public SaResult register(@ApiParam(value = "辅导员对象", required = true)@RequestBody Counselors counselors) {
        return counselorsService.register(counselors);
    }

    /**
     * 通过身份证查询辅导员报名信息
     * @param idCard 身份证号
     * @return cn.dev33.satoken.util.SaResult
     * 访问地址：http://localhost:8082/counselors/queryByIdCard
     */
    @SaCheckRole("school")
    @ApiOperation(value = "通过身份证查询辅导员报名信息")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok",response = CounselorsVo.class),
            @ApiResponse(code = 500, message = "msg"),
            @ApiResponse(code = 500, message = "登录异常"),
            @ApiResponse(code = 500, message = "缺少角色权限"),
            @ApiResponse(code = 501, message = "没有相关信息")
    })
    @GetMapping("/queryByIdCard")
    public SaResult queryByIdCard(@ApiParam(value = "身份证号", required = true)String idCard) {
        return counselorsService.queryByIdCard(idCard);
    }

    /**
     * 通过名字查询辅导员报名信息
     * @param name 辅导员姓名
     * @return cn.dev33.satoken.util.SaResult
     * 访问地址：http://localhost:8082/counselors/queryByName
     */
    @SaCheckRole("school")
    @ApiOperation(value = "通过名字查询辅导员报名信息")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok",response = CounselorsVo.class),
            @ApiResponse(code = 500, message = "msg"),
            @ApiResponse(code = 500, message = "登录异常"),
            @ApiResponse(code = 500, message = "缺少角色权限"),
            @ApiResponse(code = 501, message = "没有相关信息")
    })
    @GetMapping("/queryByName")
    public SaResult queryByName(@ApiParam(value = "姓名", required = true)String name) {
        return counselorsService.queryByName(name);
    }


    /**
     * 删除辅导员报名信息
     * @param name 辅导员姓名
     * @param idCard 身份证号
     * @return cn.dev33.satoken.util.SaResult
     * 访问地址：http://localhost:8082/counselors/queryAll
     */
    @SaCheckRole("school")
    @ApiOperation(value = "删除辅导员报名信息")
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功"),
            @ApiResponse(code = 500, message = "msg"),
            @ApiResponse(code = 500, message = "登录异常"),
            @ApiResponse(code = 500, message = "缺少角色权限"),
            @ApiResponse(code = 500, message = "未在该校报名人员中找到该辅导员")
    })
    @PostMapping("delete")
    public SaResult delete(@ApiParam(value = "姓名", required = true)String name, @ApiParam(value = "身份证号", required = true) String idCard) {
        return counselorsService.delete(name, idCard);
    }
}
