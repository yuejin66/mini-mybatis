package com.yuejin66.mybatis;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lyj
 */
public class SqlSessionFactoryBuilder {

    /**
     * 构建实例化元素
     * 用来解析 xml 文件，以及 初始化 SqlSession 工厂类 DefaultSqlSessionFactory。
     *
     * @param reader
     * @return
     */
    public DefaultSqlSessionFactory build(Reader reader) {
        SAXReader saxReader = new SAXReader();
        try {
            // saxReader.setEntityResolver(new XMLMapperEntityResolver()); // 不联网也能解析 xml
            Document document = saxReader.read(new InputSource(reader));
            Configuration configuration = parseConfiguration(document.getRootElement());
            return new DefaultSqlSessionFactory(configuration);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 解析配置。获取两个配置，数据库的链接信息 dataSource 和对数据库操作语句的解析 mappers。
    private Configuration parseConfiguration(Element root) {
        Configuration configuration = new Configuration();
        configuration.setDataSource(dataSource(root.selectNodes("//dataSource")));
        configuration.setConnection(connection(configuration.dataSource));
        configuration.setMapperElement(mapperElement(root.selectNodes("mappers")));
        return configuration;
    }

    // 获取数据源配置信息
    private Map<String, String> dataSource(List<Node> list) {
        HashMap<String, String> dataSource = new HashMap<>(4);
        for (Node node : list) {
            Element element = (Element) node;
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            dataSource.put(name, value);
        }
        return dataSource;
    }

    // 链接数据库。如果要链接多套数据库可以在这里扩展。
    private Connection connection(Map<String, String> dataSource) {
        try {
            Class.forName(dataSource.get("driver"));
            return DriverManager.getConnection(dataSource.get("url"), dataSource.get("username"), dataSource.get("password"));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 解析 SQL 语句
    private Map<String, XNode> mapperElement(List<Node> list) {
        Map<String, XNode> map = new HashMap<>();

        for (Node n : list) {
            Element element_1 = (Element) n;
            String resource = element_1.attributeValue("resource");
            try {
                Reader reader = Resources.getResourceAsReader(resource);
                SAXReader saxReader = new SAXReader();
                Document document = saxReader.read(new InputSource(reader));
                Element root = document.getRootElement();
                String namespace = root.attributeValue("namespace");

                // SELECT
                List<Node> selectNodes = root.selectNodes("select");
                for (Node node : selectNodes) {
                    Element element_2 = (Element) node;
                    String id = element_2.attributeValue("id");
                    String parameterType = element_2.attributeValue("parameterType");
                    String resultType = element_2.attributeValue("resultType");
                    String sql = element_2.getText();
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
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
