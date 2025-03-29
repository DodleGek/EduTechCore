package com.sspu.exception;

public class EduxException extends RuntimeException {
    private String errMessage;

    public EduxException(String errMessage) {
        super(errMessage);
        this.errMessage = errMessage;
    }

    public static void cast(CommonError commonError) {
        throw new EduxException(commonError.getErrMessage());
    }

    public static void cast(String errMessage) {
        throw new EduxException(errMessage);
    }

    public String getErrMessage() {
        return errMessage;
    }
}
