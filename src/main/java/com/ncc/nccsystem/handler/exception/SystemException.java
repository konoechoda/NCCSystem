package com.ncc.nccsystem.handler.exception;


import com.ncc.nccsystem.enums.AppHttpCodeEnum;

/**
 * 统一异常处理
 */
public class SystemException extends RuntimeException{

    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public SystemException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMsg());
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }

    public SystemException(String msg) {
        super(msg);
        this.code = AppHttpCodeEnum.SYSTEM_ERROR.getCode();
        this.msg = msg;
    }

}

