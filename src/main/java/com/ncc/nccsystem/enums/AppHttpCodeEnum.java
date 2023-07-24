package com.ncc.nccsystem.enums;

public enum AppHttpCodeEnum {
    SYSTEM_ERROR(500,"出现错误"),
    NO_INFORMATION(501, "没有相关信息"),
    REQUIRE_USERNAME(502, "必需填写用户名");
    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}