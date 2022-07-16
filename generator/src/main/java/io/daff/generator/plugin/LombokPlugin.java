package io.daff.generator.plugin;

import io.daff.generator.config.LombokConfig;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
 * mybatis逆向工程整合lombok插件
 *
 * @author daff
 * @since 2020/1/25
 */
public class LombokPlugin extends PluginAdapter {

    private final LombokConfig lombokConfig;

    public LombokPlugin() {
        lombokConfig = new LombokConfig();
    }

    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        // 引入注解相关的包
        topLevelClass.addImportedType("lombok.Data");
        topLevelClass.addAnnotation("@Data");

        topLevelClass.addImportedType("lombok.Builder");
        topLevelClass.addAnnotation("@Builder");

        topLevelClass.addImportedType("lombok.NoArgsConstructor");
        topLevelClass.addAnnotation("@NoArgsConstructor");

        topLevelClass.addImportedType("lombok.AllArgsConstructor");
        topLevelClass.addAnnotation("@AllArgsConstructor");

        topLevelClass.addImportedType("lombok.experimental.Accessors");
        topLevelClass.addAnnotation("@Accessors(chain = true)");

        if (lombokConfig.getHasParent()) {
            topLevelClass.addImportedType("lombok.EqualsAndHashCode");
            topLevelClass.addAnnotation("@EqualsAndHashCode(callSuper = true)");
        }

        return true;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

}
