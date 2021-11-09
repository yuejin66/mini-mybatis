package com.yuejin66.mapper;

import com.yuejin66.po.User;

import java.util.List;

/**
 * @author lyj
 */
public interface UserMapper {

    List<User> queryUserInfo(User user);

    User queryUserInfoById(Long id);
}
