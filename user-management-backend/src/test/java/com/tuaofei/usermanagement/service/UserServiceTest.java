package com.tuaofei.usermanagement.service;

import com.tuaofei.usermanagement.model.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TODO 类描述
 *
 * @date 2023/6/10 21:38
 */
@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void test(){
        User user = new User();
        user.setUserName("");
        user.setUserAccount("");
        user.setAvatarUrl("");
        user.setGender(0);
        user.setUserPassword("");
        user.setPhone("");
        user.setEmail("");
        user.setUserStatus(0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setIsDelete(0);
        user.setUserRole(0);
        user.setPlanetCode("");
        boolean save = userService.save(user);
        System.out.println(user.getId());
        assertTrue(save);
    }

    @Test
    void userRegister() {
        String account = "tuaofei";
        String pwd = "12345678";
        String checkWord = "12345678";
        long result = userService.userRegister(account, pwd, checkWord);
        Assertions.assertTrue(result > 0);

    }
}