package io.daff.generator.util;

import io.daff.generator.config.CommentConfig;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * mybatis逆向工程配置文件的信息抽取工具类
 *
 * @author daff
 * @since 2020/1/30
 */
public class ConfigExtractUtils {

    private static final CommentConfig commentConfig = new CommentConfig();

    public static Map<String, String> getConfig(PluginAdapter pa, IntrospectedTable it) {
        String targetPackage = pa.getContext().getJavaClientGeneratorConfiguration().getTargetPackage();
        String mapperType = it.getMyBatis3JavaMapperType();
        String topPackage = targetPackage.substring(0, targetPackage.lastIndexOf('.'));
        String entityClassName = mapperType.substring(mapperType.lastIndexOf('.') + 1).replace("Mapper", "");
        String targetProject = pa.getContext().getJavaClientGeneratorConfiguration().getTargetProject();

        Map<String, String> config = new HashMap<>();
        config.put("topPackage", topPackage);
        config.put("entityClassName", entityClassName);
        config.put("entityInstanceName", Character.toLowerCase(entityClassName.charAt(0)) +
                entityClassName.substring(1));
        config.put("targetProject", targetProject);
        config.put("author", commentConfig.getAuthor());
        config.put("date",commentConfig.getDate());

        return config;
    }

}
