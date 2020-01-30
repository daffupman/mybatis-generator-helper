package io.daff.mybatis.plugin;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.daff.mybatis.util.ConfigExtractUtils;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自动生成controller层代码
 *
 * @author daffupman
 * @since 2020/1/30
 */
public class ControllerPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {

        Map<String, String> config = ConfigExtractUtils.getConfig(this, introspectedTable);

        try {
            generateController(config);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @SuppressWarnings("deprecation")
    private void generateController(Map<String, String> config) throws IOException {

        String targetProject = config.get("targetProject");
        String topPackage = config.get("topPackage");
        String entityClassName = config.get("entityClassName");

        String dirPath = targetProject + "/" + topPackage.replaceAll("\\.", "/") + "/controller";
        String filePath = targetProject + "/" + topPackage.replaceAll("\\.", "/") + "/controller/" + entityClassName
                + "Controller.java";
        File dir = new File(dirPath);
        File file = new File(filePath);
        if (!dir.exists()) {
            // 包不存在则新建
            if (!dir.mkdirs() || !file.createNewFile()) {
                throw new RuntimeException("create directory or file failed.");
            }
        }

        Configuration cfg = new Configuration();
        cfg.setClassForTemplateLoading(this.getClass(), "/ftl");
        cfg.setObjectWrapper(new DefaultObjectWrapper());

        try {
            Template temp = cfg.getTemplate("controller.ftl");
            Writer out = new OutputStreamWriter(new FileOutputStream(file));
            temp.process(config, out);
            out.flush();
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
        }
    }
}
