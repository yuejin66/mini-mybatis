package com.yuejin66.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 定义 YML 配置信息
 *
 * @author lyj
 */
@ConfigurationProperties(prefix = MybatisProperties.MYBATIS_PREFIX)
public class MybatisProperties {

    public static final String MYBATIS_PREFIX = "mybatis.datasource";

    private String driver;

    private String url;

    private String username;

    private String password;

    private String mapperLocations;

    private String baseDaoPackage;

    /* get/set */

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMapperLocations() {
        return mapperLocations;
    }

    public void setMapperLocations(String mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public String getBaseDaoPackage() {
        return baseDaoPackage;
    }

    public void setBaseDaoPackage(String baseDaoPackage) {
        this.baseDaoPackage = baseDaoPackage;
    }
}
