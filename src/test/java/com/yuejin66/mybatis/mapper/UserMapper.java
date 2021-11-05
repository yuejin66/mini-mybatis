package com.yuejin66.mybatis.mapper;

import com.yuejin66.mybatis.po.User;

/**
 * @author lyj
 */
public interface UserMapper {

    User queryUserInfoById(Long id);

    User queryUserList(User user);
}
