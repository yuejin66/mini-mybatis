package com.yuejin66.mybatis;

import com.yuejin66.mybatis.po.User;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * @author lyj
 */
public class ApiTest {

    @Test
    public void test_queryUserInfoById() {
        String resource = "mybatis-config.xml";
        Reader reader;
        try {
            reader = Resources.getResourceAsReader(resource);
            SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);
            SqlSession session = sqlMapper.openSession();
            try {
                User user = session.selectOne("com.yuejin66.mybatis.mapper.UserMapper.queryUserInfoById", 1L);
                System.out.println(user);
            } finally {
                session.close();
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_queryUserList() {
        String resource = "mybatis-config.xml";
        Reader reader;
        try {
            reader = Resources.getResourceAsReader(resource);
            SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);
            SqlSession session = sqlMapper.openSession();
            try {
                List<User> userList = session.selectList("com.yuejin66.mybatis.mapper.UserMapper.queryUserInfo", "团团");
                userList.forEach(System.out::println);
            } finally {
                session.close();
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
