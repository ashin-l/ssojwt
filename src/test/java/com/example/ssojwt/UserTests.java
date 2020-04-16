package com.example.ssojwt;


import javax.annotation.Resource;

import com.example.ssojwt.dao.UserDao;
import com.example.ssojwt.model.SysUser;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class UserTests {

    @Resource
    private UserDao userDao;

    @Test
    public void testInsert() {
        SysUser user = new SysUser();
        user.setUsername("user");
        user.setPassword(new BCryptPasswordEncoder().encode("111"));
        System.out.println(user);
        userDao.save(user);
    }
}