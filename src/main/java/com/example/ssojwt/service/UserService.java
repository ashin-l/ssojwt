package com.example.ssojwt.service;

import com.example.ssojwt.model.SysUser;

public interface UserService {

  SysUser getByName(String username);
}