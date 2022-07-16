package io.daff.generator.plugin;

import io.daff.generator.support.DefaultJavaMapperMethodGenerator;
import io.daff.generator.support.DefaultXmlElementGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.*;

/**
 * @author daff
 * @since 2020/10/31
 */
public class MapperInterfaceMethodRenamePlugin extends PluginAdapter {

    /**
     * 重命名
     */
    private static final Map<String, String> OVERRIDE_NAME_MAP = new HashMap<>();

    static {
        OVERRIDE_NAME_MAP.put("ByPrimaryKey", "ById");
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        AbstractXmlElementGenerator elementGenerator = new DefaultXmlElementGenerator();
        elementGenerator.setContext(context);
        elementGenerator.setIntrospectedTable(introspectedTable);
        elementGenerator.addElements(document.getRootElement());
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

        // 修改原始名称。
        overrideMethodNameAndParamName(interfaze);

        AbstractJavaMapperMethodGenerator methodGenerator = new DefaultJavaMapperMethodGenerator();
        methodGenerator.setContext(context);
        methodGenerator.setIntrospectedTable(introspectedTable);
        methodGenerator.addInterfaceElements(interfaze);
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }

    /**
     * 重写方法名和参数名。
     * xxxByPrimaryKey -> xxxById
     * record -> class.getSimpleName
     */
    private void overrideMethodNameAndParamName(Interface interfaze) {
        for (Method method : interfaze.getMethods()) {

            // 替换方法名
            String methodName = method.getName();
            for (Map.Entry<String, String> entry : OVERRIDE_NAME_MAP.entrySet()) {
                String renameBeforeKey = entry.getKey();
                String renameAfterKey = entry.getValue();
                if (methodName.contains(renameBeforeKey)) {
                    methodName = methodName.substring(0, methodName.indexOf(renameBeforeKey)) + renameAfterKey;
                }
            }
            method.setName(methodName);

            // 替换参数名称
            for (Parameter parameter : method.getParameters()) {
                String paramName = parameter.getName();
                String paramTypeName = parameter.getType().getShortName();
                if ("record".equals(paramName) && !Objects.equals(paramName, paramTypeName)) {
                    parameter = new Parameter(parameter.getType(), paramName.toLowerCase(Locale.ROOT), parameter.isVarargs());
                }
            }
        }
    }
}
