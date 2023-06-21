package com.tuaofei.usermanagement.common;

import lombok.Data;

import java.io.Serializable;

/**
 * TODO 类描述
 *
 * @date 2023/6/16 20:35
 */
@Data
public class BaseResponse<T> implements Serializable {

    private int code;
    private T data;
    private String message;
    private String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data) {
        this(code,data,"","");
    }

    public BaseResponse(CodeEnum codeEnum) {
        this(codeEnum.getCode(),null, codeEnum.getMessage(), codeEnum.getDescription());
    }
}
