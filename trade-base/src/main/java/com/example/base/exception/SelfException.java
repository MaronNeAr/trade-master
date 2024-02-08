

package com.example.base.exception;


/**
 * @description 学成在线项目异常类
 */
public class SelfException extends RuntimeException {
    private String errMessage;

    public String getErrMessage() {
        return errMessage;
    }

    public SelfException(String errMessage) {
        super(errMessage);
        this.errMessage = errMessage;
    }

    public static void cast(CommonError commonError) {
        throw new SelfException(commonError.getErrMessage());
    }

    public static void cast(String errMessage) {
        throw new SelfException(errMessage);
    }
}
