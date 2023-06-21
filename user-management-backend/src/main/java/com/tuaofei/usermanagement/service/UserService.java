package com.tuaofei.usermanagement.service;

import com.tuaofei.usermanagement.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 折腾的小飞
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2023-06-10 21:15:29
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount
     * @param passWord
     * @param checkPassWord
     * @return
     */
    long userRegister(String userAccount,String passWord,String checkPassWord);

    /**
     * 用户登录
     * @param userAccount
     * @param passWord
     * @param request
     * @return
     */
    User userLogin(String userAccount, String passWord, HttpServletRequest request);

    /**
     * 通过用户名称查询用户信息
     * @param userName
     * @return
     */
    List<User> queryUserByUserName(String userName);

    /**
     * 查询所有用户
     * @return
     */
    List<User> queryAllUser();

    /**
     * 根据信息查询用户
     * @param user
     * @return
     */
    List<User> queryAllUser(User user);

    /**
     * 用户信息脱敏
     * @param user
     * @return
     */
    User getDesensitizationUser(User user);

    /**
     * 退出登录
     * @param httpServletRequest
     * @return
     */
    boolean outLogin(HttpServletRequest httpServletRequest);
}
