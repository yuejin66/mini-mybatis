package com.yuejin66.spring;

import com.yuejin66.mybatis.SqlSession;
import com.yuejin66.mybatis.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

import javax.annotation.Resource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author lyj
 */
@SuppressWarnings("unchecked")
public class MapperFactoryBean<T> implements FactoryBean<T> {

    private Logger log = LoggerFactory.getLogger(MapperFactoryBean.class);

    private Class<T> mapperInterface;

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    public MapperFactoryBean(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    @Override
    public T getObject() throws Exception {
        InvocationHandler handler = (Proxy, method, args) -> {
            log.info("你被代理了，执行 SQL 操作！{}", method.getName());
            try {
                SqlSession session = sqlSessionFactory.openSession();
                try {
                    return session.selectOne(mapperInterface.getName() + "." + method.getName(), args[0]);
                } finally {
                    session.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return method.getReturnType().newInstance();
        };
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{mapperInterface}, handler);
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }
}
