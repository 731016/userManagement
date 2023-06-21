package com.tuaofei.usermanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuaofei.usermanagement.common.CodeEnum;
import com.tuaofei.usermanagement.contants.UserContants;
import com.tuaofei.usermanagement.execption.BusinessException;
import com.tuaofei.usermanagement.model.domain.User;
import com.tuaofei.usermanagement.service.UserService;
import com.tuaofei.usermanagement.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author 折腾的小飞
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2023-06-10 21:15:29
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    public final static String SALT = "taf";

    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String passWord, String checkPassWord) {
        if (StringUtils.isAllBlank(userAccount,passWord,checkPassWord)){
           throw new BusinessException(CodeEnum.NULL_ERROR);
        }
        if (userAccount.length() < 4){
            throw new BusinessException(CodeEnum.PARAMS_ERROR,"账户长度小于4位!");
        }
        if (passWord.length() < 8 || checkPassWord.length() < 8){
            throw new BusinessException(CodeEnum.PARAMS_ERROR,"密码或校验密码长度小于8位!");
        }
        //账户不包含特殊字符
        String reg = "^[a-zA-Z0-9_]+$";
        Pattern pattern = Pattern.compile(reg);
        //testStr被检测的文本
        Matcher matcher = pattern.matcher(userAccount);
        //匹配上的时候返回true,匹配不通过返回false
        boolean matches = matcher.matches();
        if (!matches){
            throw new BusinessException(CodeEnum.PARAMS_ERROR,"账户包含特殊字符!");
        }

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userAccount",userAccount);
        long count = userMapper.selectCount(wrapper);
        if (count > 0){
            throw new BusinessException(CodeEnum.ERROR_STATUS,"账户已存在!");
        }

        //密码和校验密码相同
        if (!StringUtils.equals(passWord,checkPassWord)){
            throw new BusinessException(CodeEnum.ERROR_STATUS,"密码和校验密码不相同!");
        }
        String salt = "taf";
        String encryptPwd = DigestUtils.md5DigestAsHex((salt + passWord).getBytes(StandardCharsets.UTF_8));
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPwd);
        boolean save = this.save(user);
        if (!save){
            throw new BusinessException(CodeEnum.SYSTEM_ERROR,"保存出错!");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String passWord, HttpServletRequest request) {
        if (StringUtils.isAllBlank(userAccount,passWord)){
            throw new BusinessException(CodeEnum.NULL_ERROR);
        }
        if (userAccount.length() < 4){
            throw new BusinessException(CodeEnum.PARAMS_ERROR,"账户长度小于4位!");
        }
        if (passWord.length() < 8){
            throw new BusinessException(CodeEnum.PARAMS_ERROR,"密码长度小于8位!");
        }
        //账户不包含特殊字符
        String reg = "^[a-zA-Z0-9_]+$";
        Pattern pattern = Pattern.compile(reg);
        //testStr被检测的文本
        Matcher matcher = pattern.matcher(userAccount);
        //匹配上的时候返回true,匹配不通过返回false
        boolean matches = matcher.matches();
        if (!matches){
            throw new BusinessException(CodeEnum.PARAMS_ERROR,"账户包含特殊字符!");
        }

        String encryptPwd = DigestUtils.md5DigestAsHex((SALT + passWord).getBytes(StandardCharsets.UTF_8));

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassWord",encryptPwd);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null){
            log.info("用户不存在或账户,密码错误");
            throw new BusinessException(CodeEnum.ERROR_STATUS,"用户不存在或账户,密码错误!");
        }

        User responseUser = getDesensitizationUser(user);

        HttpSession session = request.getSession();
        session.setAttribute(UserContants.USER_LOGIN_STATE,responseUser);

        return responseUser;
    }

    @Override
    public List<User> queryUserByUserName(String userName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("userName",userName);
        return userMapper.selectList(queryWrapper);
    }

    @Override
    public List<User> queryAllUser() {
        return userMapper.selectList(null);
    }

    @Override
    public List<User> queryAllUser(User user) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if (user != null){
            String userName = user.getUserName();
            if (StringUtils.isNoneBlank(userName)){
                userQueryWrapper.like("userName",userName);
            }
            String userAccount = user.getUserAccount();
            if (StringUtils.isNoneBlank(userAccount)){
                userQueryWrapper.like("userAccount",userAccount);
            }
            Integer gender = user.getGender();
            if (gender != null){
                userQueryWrapper.eq("gender",gender);
            }
            String phone = user.getPhone();
            if (StringUtils.isNoneBlank(phone)){
                userQueryWrapper.like("phone",phone);
            }
            String email = user.getEmail();
            if (StringUtils.isNoneBlank(email)){
                userQueryWrapper.like("email",email);
            }
            Integer userRole = user.getUserRole();
            if (userRole != null){
                userQueryWrapper.eq("userRole",userRole);
            }
            Integer userStatus = user.getUserStatus();
            if (userStatus != null){
                userQueryWrapper.eq("userStatus",userStatus);
            }
        }
        return userMapper.selectList(userQueryWrapper);
    }

    @Override
    public User getDesensitizationUser(User user){
        if (user == null){
            throw new BusinessException(CodeEnum.NULL_ERROR);
        }
        User responseUser = new User();
        responseUser.setId(user.getId());
        responseUser.setUserName(user.getUserName());
        responseUser.setUserAccount(user.getUserAccount());
        responseUser.setAvatarUrl(user.getAvatarUrl());
        responseUser.setGender(user.getGender());
        responseUser.setPhone(user.getPhone());
        responseUser.setEmail(user.getEmail());
        responseUser.setUserStatus(user.getUserStatus());
        responseUser.setCreateTime(user.getCreateTime());
        responseUser.setUserRole(user.getUserRole());
        responseUser.setPlanetCode(user.getPlanetCode());
        return responseUser;
    }

    @Override
    public boolean outLogin(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        session.removeAttribute(UserContants.USER_LOGIN_STATE);
        return true;
    }
}




