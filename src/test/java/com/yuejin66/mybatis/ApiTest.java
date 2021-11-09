package com.yuejin66.mybatis;

import com.yuejin66.mybatis.mapper.UserMapper;
import com.yuejin66.mybatis.po.User;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
                User user = session.selectOne("com.yuejin66.com.yuejin66.mybatis.mapper.UserMapper.queryUserInfoById", 1L);
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
                List<User> userList = session.selectList("com.yuejin66.com.yuejin66.mybatis.mapper.UserMapper.queryUserInfo", "团团");
                userList.forEach(System.out::println);
            } finally {
                session.close();
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_classPathXmlApplicationContest() {
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("spring-config.xml");
        UserMapper userMapper = beanFactory.getBean("userMapper", UserMapper.class);
        User user = userMapper.queryUserInfoById(1L);
        System.out.println("测试结果：" + user);
    }
}
