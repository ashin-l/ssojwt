package com.example.ssojwt.security;

import lombok.Data;

@Data
public class LoginUser {

  private String username;
  private String password;
  private Boolean rememberMe;

}