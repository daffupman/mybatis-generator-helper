package io.daff.mybatis.plugin;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;

import java.util.Iterator;
import java.util.List;

/**
 * @author daffupman
 * @since 2020/10/31
 */
public class DefaultJavaMapperMethodGenerator extends AbstractJavaMapperMethodGenerator {
    @Override
    public void addInterfaceElements(Interface interfaze) {

        resetInterface(interfaze);

        addInterfaceBatchInsert(interfaze);
        addInterfaceDeleteByIds(interfaze);
        addInterfaceSelect(interfaze);
        addInterfaceSelectByIds(interfaze);
        addInterfaceBatchUpdate(interfaze);
    }

    private void addInterfaceSelectByIds(Interface interfaze) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.DEFAULT);
        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        returnType.addTypeArgument(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
        method.setReturnType(returnType);
        method.setName("selectByIds");
        FullyQualifiedJavaType paramType = FullyQualifiedJavaType.getNewListInstance();

        List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();
        if (primaryKeyColumns == null || primaryKeyColumns.size() > 1) {
            throw new IllegalArgumentException("table only has one id");
        }

        paramType.addTypeArgument(primaryKeyColumns.get(0).getFullyQualifiedJavaType());
        method.addParameter(new Parameter(paramType, "ids"));

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
            interfaze.addMethod(method);
        }
    }

    private void addInterfaceSelect(Interface interfaze) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.DEFAULT);
        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        returnType.addTypeArgument(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
        method.setReturnType(returnType);
        method.setName("select");
        FullyQualifiedJavaType paramType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        method.addParameter(new Parameter(paramType, "record"));

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
            interfaze.addMethod(method);
        }
    }

    private void addInterfaceBatchUpdate(Interface interfaze) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.DEFAULT);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName("batchUpdate");
        FullyQualifiedJavaType paramType = FullyQualifiedJavaType.getNewListInstance();
        paramType.addTypeArgument(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
        method.addParameter(new Parameter(paramType, "records"));

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
            interfaze.addMethod(method);
        }
    }

    private void addInterfaceDeleteByIds(Interface interfaze) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.DEFAULT);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName("deleteByIds");

        List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();
        if (primaryKeyColumns == null || primaryKeyColumns.size() > 1) {
            throw new IllegalArgumentException("table only has one id");
        }

        FullyQualifiedJavaType paramType = FullyQualifiedJavaType.getNewListInstance();
        paramType.addTypeArgument(primaryKeyColumns.get(0).getFullyQualifiedJavaType());
        method.addParameter(new Parameter(paramType, "ids"));

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
            interfaze.addMethod(method);
        }
    }

    private void addInterfaceDelete(Interface interfaze) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.DEFAULT);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName("delete");
        FullyQualifiedJavaType paramType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        method.addParameter(new Parameter(paramType, "record"));

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
            interfaze.addMethod(method);
        }
    }

    private void addInterfaceBatchInsert(Interface interfaze) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.DEFAULT);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName("batchInsert");
        FullyQualifiedJavaType paramType = FullyQualifiedJavaType.getNewListInstance();
        paramType.addTypeArgument(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
        method.addParameter(new Parameter(paramType, "records"));

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
            interfaze.addMethod(method);
        }
    }

    /**
     * 重命名默认生成的mapper方法名称
     */
    private void resetInterface(Interface interfaze) {
        Iterator<Method> iterator = interfaze.getMethods().iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }
}
