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
 * 自动生成Service层代码
 *
 * @author daffupman
 * @since 2020/1/30
 */
public class ServicePlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {

        Map<String, String> config = ConfigExtractUtils.getConfig(this, introspectedTable);

        try {
            generateService(config);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void generateService(Map<String, String> config) throws IOException {

        this.load(config, true);
        this.load(config, false);
    }

    /**
     * 加载模板，并生成模板代码
     *
     * @param config            mbg的配置信息
     * @param isInterface       是否是接口
     */
    private void load(Map<String, String> config, Boolean isInterface) throws IOException {

        String topPackage = config.get("topPackage");
        String entityClassName = config.get("entityClassName");
        String targetProject = config.get("targetProject");

        // service层的包名
        String servicePath = targetProject + "/" + topPackage.replaceAll("\\.", "/") + "/service";

        String dirPath;
        String filePath;
        if (isInterface) {
            dirPath = servicePath + "/";
            filePath = servicePath + "/" + entityClassName + "Service.java";
        } else {
            dirPath = servicePath + "/impl";
            filePath = servicePath + "/impl/" + entityClassName + "ServiceImpl.java";
        }
        File dir = new File(dirPath);
        File impl = new File(filePath);

        if (!dir.exists()) {
            // 包不存在则新建
            if (!dir.mkdirs() || !impl.createNewFile()) {
                throw new RuntimeException("create directory or file failed.");
            }
        }

        // 读取模板，并输出service接口类和service接口实现类
        Configuration cfg2 = new Configuration();
        cfg2.setClassForTemplateLoading(this.getClass(), "/ftl");
        cfg2.setObjectWrapper(new DefaultObjectWrapper());
        try {
            // 输出service接口实现类
            String ftlName = isInterface ? "service.ftl" : "service-impl.ftl";
            Template serviceImplTemp = cfg2.getTemplate(ftlName);
            Writer serviceImplOut = new OutputStreamWriter(new FileOutputStream(impl));
            serviceImplTemp.process(config, serviceImplOut);
            serviceImplOut.flush();
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
        }
    }

}
