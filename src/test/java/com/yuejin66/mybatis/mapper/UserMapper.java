package com.yuejin66.mybatis.mapper;

import com.yuejin66.mybatis.po.User;

import java.util.List;

/**
 * @author lyj
 */
public interface UserMapper {

    List<User> queryUserInfo(User user);

    User queryUserInfoById(Long id);
}
