package com.example.ssojwt.controller;

import javax.annotation.Resource;

import com.example.ssojwt.service.IndexService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Index {

    @Resource
    IndexService indexService;

    @GetMapping("/index")
    public String index() {
        return "ssojwt index";
    }

    @GetMapping("/home")
    public String home() {
        return "in home aaa";
    }
}