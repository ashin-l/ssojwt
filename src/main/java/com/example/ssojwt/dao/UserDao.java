package com.example.ssojwt.dao;

import com.example.ssojwt.model.SysUser;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
  SysUser getByName(String username);
  int save(SysUser user);
}