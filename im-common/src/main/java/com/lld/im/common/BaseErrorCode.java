package com.lld.im.common;

import com.lld.im.common.exception.ApplicationExceptionEnum;

/**
 * @author tangcj
 * @date 2023/05/25 20:58
 **/
public enum BaseErrorCode implements ApplicationExceptionEnum {

    SUCCESS(200, "success"),

    SYSTEM_ERROR(90000, "服务器内部错误,请联系管理员"),

    PARAMETER_ERROR(90001, "参数校验错误"),
    ;

    private int code;

    private String error;

    BaseErrorCode(int code, String error) {
        this.code = code;
        this.error = error;
    }

    @Override
    public int getCode() {
        return 0;
    }

    @Override
    public String getError() {
        return null;
    }
}
