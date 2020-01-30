package io.daff.mybatis.config;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * 注释模型
 *
 * @author daffupman
 * @since 2020/1/26
 */
@Data
public class CommentConfig {

    /**
     * 作者信息
     */
    private String author;

    /**
     * 日期
     */
    private String date;

    public CommentConfig() {
        this.loadProperties();
    }

    /**
     * 读取classpath下的comment.properties文件
     * 构造注释信息
     */
    private void loadProperties() {
        Properties prop;
        try {
            // 读取comment配置信息
            prop = new Properties();
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("comment.properties");
            prop.load(is);

            // 初始化属性值
            this.author = prop.getProperty("author");
            this.date = new SimpleDateFormat(prop.getProperty("date_pattern"))
                    .format(new Date());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
