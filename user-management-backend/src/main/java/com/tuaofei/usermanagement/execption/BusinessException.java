package com.tuaofei.usermanagement.execption;

import com.tuaofei.usermanagement.common.CodeEnum;

/**
 * TODO 类描述
 *
 * @date 2023/6/16 21:50
 */
public class BusinessException extends RuntimeException{
    private final int code;
    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(CodeEnum errorCode) {
        super(errorCode.getDescription());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(CodeEnum errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }


    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
