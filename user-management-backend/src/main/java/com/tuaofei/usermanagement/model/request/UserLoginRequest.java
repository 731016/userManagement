package com.tuaofei.usermanagement.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * TODO 类描述
 *
 * @author 折腾的小飞
 * @date 2023/6/11 22:19
 */
@Data
public class UserLoginRequest implements Serializable {
    private String userAccount;
    private String passWord;
}
