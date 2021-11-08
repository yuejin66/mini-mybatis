package com.yuejin66.mybatis.mapper;

import com.yuejin66.mybatis.po.User;

/**
 * @author lyj
 */
public interface UserMapper {

    User queryUserInfo(User user);

    User queryUserInfoById(Long id);
}
