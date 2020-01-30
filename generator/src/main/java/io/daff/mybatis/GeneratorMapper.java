package io.daff.mybatis;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * mybatis逆向工程的入口程序
 *
 * @author daffupman
 * @since 2020/1/25
 */
public class GeneratorMapper {

    public static void main(String[] args) throws Exception {

        List<String> warnings = new ArrayList<>();
        boolean overwrite = true;
        // 指定mybatis逆向工程的配置文件 mybatis-generator.xml
        File configFile = new File("generator/src/main/resources/mybatis-generator.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }
}
