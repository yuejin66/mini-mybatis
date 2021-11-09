package com.yuejin66.autoconfigure;

import com.yuejin66.mybatis.SqlSessionFactory;
import com.yuejin66.mybatis.SqlSessionFactoryBuilder;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import com.yuejin66.spring.MapperFactoryBean;
import com.yuejin66.spring.MapperScannerConfigurer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 加载配置和初始化 Bean
 *
 * @author lyj
 */
@Configuration
@ConditionalOnClass({SqlSessionFactory.class})
@EnableConfigurationProperties(MybatisProperties.class)
public class MybatisAutoConfiguration implements InitializingBean {

    // 实例化 SqlSession 链接工厂
    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory(Connection connection, MybatisProperties mybatisProperties) throws IOException, DocumentException {
        return new SqlSessionFactoryBuilder().build(connection, mybatisProperties.getMapperLocations());
    }

    // 实例化数据库链接
    @Bean
    @ConditionalOnMissingBean
    public Connection connection(MybatisProperties mybatisProperties) {
        try {
            Class.forName(mybatisProperties.getDriver());
            return DriverManager.getConnection(mybatisProperties.getUrl(),
                    mybatisProperties.getUsername(),
                    mybatisProperties.getPassword());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 注册 Bean 信息优先于 Bean 的实例化，所以这里通过实现 EnvironmentAware 来读取 yml 中的配置信息，
    // 因为这个配置信息是用于扫描 DAO 接口类，用于注册 Bean 的。
    public static class AutoConfiguredMapperScannerRegistrar implements EnvironmentAware, ImportBeanDefinitionRegistrar {

        private String basePackage;

        @Override
        public void setEnvironment(Environment environment) {
            this.basePackage = environment.getProperty("mybatis.datasource.base-dao-package");
        }

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class);
            builder.addPropertyValue("basePackage", basePackage);
            registry.registerBeanDefinition(MapperScannerConfigurer.class.getName(), builder.getBeanDefinition());
        }
    }

    // 用于扫描加载配置和启动初始化的类。
    @Configuration
    @Import(AutoConfiguredMapperScannerRegistrar.class)
    @ConditionalOnMissingBean({MapperFactoryBean.class, MapperScannerConfigurer.class})
    public static class MapperScannerRegistrarNoConfiguration implements InitializingBean {

        @Override
        public void afterPropertiesSet() throws Exception {

        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
