package io.daff.mybatis.plugin;

import io.daff.mybatis.config.CommentConfig;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
 * 注释插件
 *
 * @author daffupman
 * @since 2020/1/25
 */
public class CommentPlugin extends PluginAdapter {

    /**
     * 注释模板模型
     */
    private CommentConfig commentModel;

    public CommentPlugin() {
        commentModel = new CommentConfig();
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

        // 类注释信息
        String sb = "/**\n" +
                " * " + introspectedTable.getFullyQualifiedTable() + "表的实体类\n" +
                " *\n" +
                " * @author "+ commentModel.getAuthor() +"\n" +
                " * @since " + commentModel.getDate() + "\n" +
                " */";
        topLevelClass.addJavaDocLine(sb);

        return true;
    }

    // TODO 如果采用通用mapper，这里需要打开
//    @Override
//    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
//
//        StringBuilder sb = new StringBuilder();
//        field.addJavaDocLine("/**");
//        sb.append(" * ");
//        sb.append(introspectedColumn.getRemarks());
//        field.addJavaDocLine(sb.toString().replace("\n", " "));
//        field.addJavaDocLine(" */");
//
//        return true;
//    }
}
