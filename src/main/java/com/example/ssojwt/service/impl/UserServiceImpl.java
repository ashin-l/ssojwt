package com.example.ssojwt.service.impl;

import com.example.ssojwt.dao.UserDao;
import com.example.ssojwt.model.SysUser;
import com.example.ssojwt.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserDao userDao;

  @Override
  public SysUser getByName(String username) {
    return userDao.getByName(username);
  }

}