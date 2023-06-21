package com.tuaofei.usermanagement.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * TODO 类描述
 *
 * @author 折腾的小飞
 * @date 2023/6/11 22:12
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 1464916134942757665L;

    private String userAccount;
    private String passWord;
    private String checkPassWord;
}
