package com.tuaofei.usermanagement.execption;

import com.tuaofei.usermanagement.common.BaseResponse;
import com.tuaofei.usermanagement.common.CodeEnum;
import com.tuaofei.usermanagement.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @date 2023/6/16 22:34
 */
@RestControllerAdvice
@Slf4j
public class GlobalExecptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("businessException: " + e.getMessage() + ",描述:" + e.getDescription(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return ResultUtils.error(CodeEnum.SYSTEM_ERROR, e.getMessage(), "");
    }

}
