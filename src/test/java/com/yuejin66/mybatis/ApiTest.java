package com.yuejin66.mybatis;

import com.yuejin66.mybatis.po.User;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;

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
                User user = session.selectOne("com.yuejin66.mybatis.dao.UserDao.queryUserInfoById", 1L);
                System.out.println(user);
            } finally {
                session.close();
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
