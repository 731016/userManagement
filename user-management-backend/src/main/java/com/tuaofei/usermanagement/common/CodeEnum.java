package com.tuaofei.usermanagement.common;

/**
 * TODO 类描述
 *
 * @date 2023/6/16 21:05
 */
public enum CodeEnum {

    SUCCESS_STATUS(20000,"操作成功",""),
    ERROR_STATUS(40400,"操作失败",""),

    PARAMS_ERROR(40000, "请求参数错误", ""),
    NULL_ERROR(40001, "请求数据为空", ""),
    NOT_LOGIN(40100, "未登录", ""),
    NO_AUTH(40101, "无权限", ""),
    SYSTEM_ERROR(50000, "系统内部异常", "");


    private final int code;
    private final String message;
    private final String description;

    CodeEnum(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
