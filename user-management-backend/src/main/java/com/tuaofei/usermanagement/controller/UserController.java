package com.tuaofei.usermanagement.controller;

import com.tuaofei.usermanagement.common.BaseResponse;
import com.tuaofei.usermanagement.common.CodeEnum;
import com.tuaofei.usermanagement.common.ResultUtils;
import com.tuaofei.usermanagement.contants.UserContants;
import com.tuaofei.usermanagement.execption.BusinessException;
import com.tuaofei.usermanagement.model.domain.User;
import com.tuaofei.usermanagement.model.request.UserLoginRequest;
import com.tuaofei.usermanagement.model.request.UserRegisterRequest;
import com.tuaofei.usermanagement.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO 类描述
 *
 * @author 折腾的小飞
 * @date 2023/6/11 22:06
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse userRegister(@RequestBody UserRegisterRequest request) {
        if (request == null) {
            throw new BusinessException(CodeEnum.NULL_ERROR);
        }
        String userAccount = request.getUserAccount();
        String passWord = request.getPassWord();
        String checkPassWord = request.getCheckPassWord();
        if (StringUtils.isAnyBlank(userAccount, passWord, checkPassWord)) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR);
        }
        long registerSign = userService.userRegister(userAccount, passWord, checkPassWord);
        return ResultUtils.success(registerSign);
    }

    @PostMapping("/login")
    public BaseResponse userLogin(@RequestBody UserLoginRequest request, HttpServletRequest httpServletRequest) {
        if (request == null) {
            throw new BusinessException(CodeEnum.NULL_ERROR);
        }
        String userAccount = request.getUserAccount();
        String passWord = request.getPassWord();
        if (StringUtils.isAllBlank(userAccount, passWord)) {
            throw new BusinessException(CodeEnum.NULL_ERROR);
        }
        User user = userService.userLogin(userAccount, passWord, httpServletRequest);
        return ResultUtils.success(user);
    }

    @PostMapping("/outLogin")
    public BaseResponse userOutLogin(HttpServletRequest httpServletRequest) {
        if (httpServletRequest == null){
            throw new BusinessException(CodeEnum.NULL_ERROR);
        }
        boolean outLoginSign = userService.outLogin(httpServletRequest);
        return ResultUtils.error(outLoginSign);
    }

    @GetMapping("queryUserByUserName")
    public BaseResponse queryUserByUserName(String userName, HttpServletRequest httpServletRequest) {
        List<User> userList = new ArrayList<>();
        if (checkRole(httpServletRequest)) {
            throw new BusinessException(CodeEnum.NO_AUTH);
        }
        if (StringUtils.isNoneBlank(userName)) {
            userList = userService.queryUserByUserName(userName);
            userList = userList.stream().map(item -> userService.getDesensitizationUser(item)).collect(Collectors.toList());
        }
        return ResultUtils.success(userList);
    }

    @PostMapping("queryAllUser")
    public BaseResponse queryAllUser(@RequestBody User user, HttpServletRequest httpServletRequest) {
        List<User> userList = new ArrayList<>();
        if (checkRole(httpServletRequest)) {
            throw new BusinessException(CodeEnum.NO_AUTH);
        }
        userList = userService.queryAllUser(user);
        userList = userList.stream().map(item -> userService.getDesensitizationUser(item)).collect(Collectors.toList());
        return ResultUtils.success(userList);
    }

    @PostMapping("deleteUserById")
    public BaseResponse deleteUserById(@RequestBody Long id, HttpServletRequest httpServletRequest) {
        if (checkRole(httpServletRequest)) {
            throw new BusinessException(CodeEnum.NO_AUTH);
        }
        if (id <= 0) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR);
        }
        boolean removeSign = userService.removeById(id);
        return ResultUtils.success(removeSign);
    }

    @PostMapping("/getCurrentUser")
    public BaseResponse getCurrentUser(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        Object userObj = session.getAttribute(UserContants.USER_LOGIN_STATE);
        if (userObj != null) {
            User user = (User) userObj;
            //attribute数据可能不是最新的
            User dbUser = userService.getById(user.getId());
            User desensitizationUser = userService.getDesensitizationUser(dbUser);
            return ResultUtils.success(desensitizationUser);
        }
        throw new BusinessException(CodeEnum.NOT_LOGIN);
    }

    @PostMapping("/delUserById")
    public BaseResponse delUserById(@RequestBody User user, HttpServletRequest httpServletRequest){
        if (user == null){
            throw new BusinessException(CodeEnum.NULL_ERROR);
        }
        if (checkRole(httpServletRequest)) {
            throw new BusinessException(CodeEnum.NO_AUTH);
        }
        Long id = user.getId();
        if (id <= 0) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR);
        }
        boolean removeSign = userService.removeById(id);
        if (removeSign){
            return ResultUtils.success(null);
        }else{
            return ResultUtils.error(null,"未知错误,删除用户失败");
        }
    }

    @PostMapping("/updateUserById")
    public BaseResponse updateUserById(@RequestBody User user, HttpServletRequest httpServletRequest) {
        if (user == null) {
            throw new BusinessException(CodeEnum.NULL_ERROR);
        }
        if (checkRole(httpServletRequest)) {
            throw new BusinessException(CodeEnum.NO_AUTH);
        }
        Long id = user.getId();
        if (id <= 0) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR);
        }
        User oldUser = userService.getById(id);
        String avatarUrl = user.getAvatarUrl();
        if (StringUtils.isNotBlank(avatarUrl)) {
            oldUser.setAvatarUrl(avatarUrl);
        }
        Integer gender = user.getGender();
        if (gender != null) {
            oldUser.setGender(gender);
        }
        String phone = user.getPhone();
        if (StringUtils.isNotBlank(phone)) {
            oldUser.setPhone(phone);
        }
        String email = user.getEmail();
        if (StringUtils.isNotBlank(email)) {
            oldUser.setEmail(email);
        }
        String userName = user.getUserName();
        if (StringUtils.isNotBlank(userName)){
            oldUser.setUserName(userName);
        }
        boolean updateSign = userService.updateById(oldUser);
        if (updateSign) {
            return ResultUtils.success(updateSign);
        }
        return ResultUtils.error(null, "未知错误,更新用户信息失败");
    }

    /**
     * 检查用户权限
     * 是管理员=1,否则为0
     * @param httpServletRequest
     * @return
     */
    private boolean checkRole(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        Object attribute = session.getAttribute(UserContants.USER_LOGIN_STATE);
        User user = (User) attribute;
        if (user == null || UserContants.USER_MANAGEMENT_ROLE != user.getUserRole()) {
            return true;
        }
        return false;
    }
}
