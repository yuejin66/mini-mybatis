package com.yuejin66.controller;

import com.yuejin66.mapper.UserMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yuejin66.po.User;

import javax.annotation.Resource;

/**
 * @author lyj
 */
@RestController
public class UserController {

    @Resource
    private UserMapper userMapper;

    @GetMapping("/api/queryUserInfoById")
    public User queryUserInfoById() {
        return userMapper.queryUserInfoById(1L);
    }
}
