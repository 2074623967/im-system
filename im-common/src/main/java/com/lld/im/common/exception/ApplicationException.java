package com.lld.im.common.exception;

/**
 * @author tangcj
 * @date 2023/05/25 20:58
 **/
public class ApplicationException extends RuntimeException {

    private int code;

    private String error;

    public ApplicationException(int code, String error) {
        super(error);
        this.code = code;
        this.error = error;
    }

    public ApplicationException(ApplicationExceptionEnum exceptionEnum) {
        super(exceptionEnum.getError());
        this.code = exceptionEnum.getCode();
        this.error = exceptionEnum.getError();
    }

    public int getCode() {
        return code;
    }

    public String getError() {
        return error;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
