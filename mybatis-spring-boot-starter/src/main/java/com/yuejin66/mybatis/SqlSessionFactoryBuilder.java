package com.yuejin66.mybatis;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lyj
 */
@SuppressWarnings("unchecked")
public class SqlSessionFactoryBuilder {

    /**
     * 改造成更符合 YML 配置方式的加载处理
     *
     * @param connection         数据库链接
     * @param packageSearchPath  xml包的路径信息
     * @return                   factory
     * @throws IOException       IOException
     * @throws DocumentException DocumentException
     */
    public DefaultSqlSessionFactory build(Connection connection, String packageSearchPath) throws IOException, DocumentException {
        Configuration configuration = new Configuration();
        configuration.setConnection(connection);
        // 读取配置
        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
        List<Element> list = new ArrayList<>(resources.length);
        for (Resource resource : resources) {
            Document document = new SAXReader().read(new InputSource(new InputStreamReader(resource.getInputStream())));
            list.add(document.getRootElement());
        }
        configuration.setMapperElement(mapperElement(list));
        return new DefaultSqlSessionFactory(configuration);
    }

    // 解析 SQL 语句
    private Map<String, XNode> mapperElement(List<Element> list) {
        Map<String, XNode> map = new HashMap<>();
        for (Element root : list) {
                // 命名空间
                String namespace = root.attributeValue("namespace");
                // SELECT
                List<Element> selectNodes = root.selectNodes("select");
                for (Element node : selectNodes) {
                    String id = node.attributeValue("id");
                    String parameterType = node.attributeValue("parameterType");
                    String resultType = node.attributeValue("resultType");
                    String sql = node.getText();
                    // 匹配 "?"
                    HashMap<Integer, String> parameter = new HashMap<>();
                    Pattern pattern = Pattern.compile("(#\\{(.*?)})");
                    Matcher matcher = pattern.matcher(sql);
                    for (int i = 1; matcher.find(); i++) {
                        String g1 = matcher.group(1);
                        String g2 = matcher.group(2);
                        parameter.put(i, g2);
                        sql = sql.replace(g1, "?");
                    }
                    XNode xNode = new XNode();
                    xNode.setNamespace(namespace);
                    xNode.setId(id);
                    xNode.setParameterType(parameterType);
                    xNode.setResultType(resultType);
                    xNode.setSql(sql);
                    xNode.setParameter(parameter);
                    map.put(namespace + '.' + id, xNode);
                }
        }
        return map;
    }
}
