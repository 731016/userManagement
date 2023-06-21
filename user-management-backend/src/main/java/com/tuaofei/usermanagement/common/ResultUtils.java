package com.tuaofei.usermanagement.common;

/**
 * TODO 类描述
 *
 * @date 2023/6/16 21:03
 */
public class ResultUtils {

    public static <T> BaseResponse success(T data) {
        return new BaseResponse(CodeEnum.SUCCESS_STATUS.getCode(), data, CodeEnum.SUCCESS_STATUS.getMessage(), CodeEnum.SUCCESS_STATUS.getDescription());
    }

    public static <T> BaseResponse error(T data) {
        return new BaseResponse(CodeEnum.ERROR_STATUS.getCode(), data, CodeEnum.ERROR_STATUS.getMessage(), CodeEnum.ERROR_STATUS.getDescription());
    }

    public static <T> BaseResponse error(CodeEnum codeEnum) {
        return new BaseResponse(codeEnum);
    }

    public static BaseResponse error(int code, String message, String description) {
        return new BaseResponse(code, null, message, description);
    }


    public static <T> BaseResponse error(CodeEnum codeEnum, String message, String description) {
        return new BaseResponse(codeEnum.getCode(), null, message, description);
    }

    public static <T> BaseResponse error(T data,String description) {
        return new BaseResponse(CodeEnum.ERROR_STATUS.getCode(), data, CodeEnum.ERROR_STATUS.getMessage(), description);
    }

}
