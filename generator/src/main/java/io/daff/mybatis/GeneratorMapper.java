package io.daff.mybatis;

import org.apache.commons.lang3.ArrayUtils;
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

        // 删除mapper映射文件
        File mapperDir = new File("generator/src/main/resources/mapper");
        if (mapperDir.exists()) {
            deleteFileRecursive(mapperDir);
        }

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

    private static void deleteFileRecursive(File file) {
        if(file.isDirectory()) {
            // 当前位置是一个目录
            String[] children = file.list();
            if (ArrayUtils.isNotEmpty(children)) {
                for (String child : children) {
                    deleteFileRecursive(new File(child));
                }
            }
        }
        file.delete();
    }
}
