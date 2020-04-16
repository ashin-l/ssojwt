package com.example.ssojwt.model;

import lombok.Data;

// postgres 数据库需要使用注解 @TableName(schema = "public")
@Data
public class SysUser {
    private Long id;
    private String username;
    private String password;
}