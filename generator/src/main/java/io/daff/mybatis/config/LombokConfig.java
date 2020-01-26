package io.daff.mybatis.config;

import lombok.Data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * lombok配置信息类
 *
 * @author daffupman
 * @since 2020/1/26
 */
@Data
public class LombokConfig {

    /**
     * 目标类是否需要继承父类
     */
    private Boolean hasParent;

    public LombokConfig() {
        this.loadProperties();
    }

    /**
     * 读取classpath下的comment.properties文件
     * 构造注释信息
     */
    private void loadProperties() {
        try {
            // 读取comment配置信息
            Properties prop = new Properties();
            InputStream is = this.getClass().getClassLoader()
                    .getResourceAsStream("lombok.properties");
            prop.load(is);

            // 初始化属性值
            hasParent = Boolean.valueOf(prop.getProperty("has_parent"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
