package io.daff.mybatis.plugin;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
 * 整合swagger注解的插件
 *
 * @author daffupman
 * @since 2020/1/25
 */
public class SwaggerPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.addImportedType("io.swagger.annotations.ApiModel");
        topLevelClass.addImportedType("io.swagger.annotations.ApiModelProperty");
        topLevelClass.addAnnotation(
                "@ApiModel(value = \""
                + introspectedTable.getFullyQualifiedTable() + "表的实体类"
                + "\")"
        );

        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {

        field.addAnnotation(
                "@ApiModelProperty(value = \""
                        + introspectedColumn.getRemarks()
                        + "\")"
        );
        return true;
    }
}
