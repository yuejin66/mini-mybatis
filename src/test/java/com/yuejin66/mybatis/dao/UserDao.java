package com.yuejin66.mybatis.dao;

import com.yuejin66.mybatis.po.User;

/**
 * @author lyj
 */
public interface UserDao {

    User queryUserInfoById(Long id);
}
