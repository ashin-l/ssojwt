package com.example.ssojwt.controller;

import com.example.ssojwt.model.SysUser;
import com.example.ssojwt.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

  @Autowired
  UserService service;

  @GetMapping("/info")
  public SysUser getInfo(String username) {
    return service.getByName(username);
  }
}