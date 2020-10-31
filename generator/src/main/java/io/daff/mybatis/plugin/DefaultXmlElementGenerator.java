package io.daff.mybatis.plugin;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author daffupman
 * @since 2020/10/31
 */
public class DefaultXmlElementGenerator extends AbstractXmlElementGenerator {

    @Override
    public void addElements(XmlElement parentElement) {

        // resetOriginXmlElement(parentElement);

        addBaseSelectFields(parentElement);
        addBatchInsertXmlElement(parentElement);
        addDeleteXmlElement(parentElement);
        addDeleteByIdsXmlElement(parentElement);
        addBatchUpdateById(parentElement);
        addSelectXmlElement(parentElement);
        addSelectByIds(parentElement);

        // // 增加base_query
        // XmlElement sql = new XmlElement("sql");
        // sql.addAttribute(new Attribute("id", "base_query"));
        // //在这里添加where条件
        // XmlElement selectTrimElement = new XmlElement("trim"); //设置trim标签
        // selectTrimElement.addAttribute(new Attribute("prefix", "WHERE"));
        // selectTrimElement.addAttribute(new Attribute("prefixOverrides", "AND | OR")); //添加where和and
        // StringBuilder sb = new StringBuilder();
        // for(IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
        //     XmlElement selectNotNullElement = new XmlElement("if"); //$NON-NLS-1$
        //     sb.setLength(0);
        //     sb.append("null != ");
        //     sb.append(introspectedColumn.getJavaProperty());
        //     selectNotNullElement.addAttribute(new Attribute("test", sb.toString()));
        //     sb.setLength(0);
        //     // 添加and
        //     sb.append(" and ");
        //     // 添加别名t
        //     sb.append("t.");
        //     sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
        //     // 添加等号
        //     sb.append(" = ");
        //     sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
        //     selectNotNullElement.addElement(new TextElement(sb.toString()));
        //     selectTrimElement.addElement(selectNotNullElement);
        // }
        // sql.addElement(selectTrimElement);
        // parentElement.addElement(sql);
        //
        // // 公用select
        // sb.setLength(0);
        // sb.append("select ");
        // sb.append("t.* ");
        // sb.append("from ");
        // sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        // sb.append(" t");
        // TextElement selectText = new TextElement(sb.toString());
        //
        // // 公用include
        // XmlElement include = new XmlElement("include");
        // include.addAttribute(new Attribute("refid", "base_query"));
        //
        // // 增加find
        // XmlElement find = new XmlElement("select");
        // find.addAttribute(new Attribute("id", "find"));
        // find.addAttribute(new Attribute("resultMap", "BaseResultMap"));
        // find.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
        // find.addElement(selectText);
        // find.addElement(include);
        // parentElement.addElement(find);
        //
        // // 增加list
        // XmlElement list = new XmlElement("select");
        // list.addAttribute(new Attribute("id", "list"));
        // list.addAttribute(new Attribute("resultMap", "BaseResultMap"));
        // list.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
        // list.addElement(selectText);
        // list.addElement(include);
        // parentElement.addElement(list);
        //
        // // 增加pageList
        // XmlElement pageList = new XmlElement("select");
        // pageList.addAttribute(new Attribute("id", "pageList"));
        // pageList.addAttribute(new Attribute("resultMap", "BaseResultMap"));
        // pageList.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
        // pageList.addElement(selectText);
        // pageList.addElement(include);
        // parentElement.addElement(pageList);
    }

    private void addSelectByIds(XmlElement parentElement) {

        StringBuilder sqlBuilder = new StringBuilder(100);
        String baseSelectFields = introspectedTable.getAllColumns().stream()
                .map(MyBatis3FormattingUtilities::getEscapedColumnName).collect(Collectors.joining(", "));
        sqlBuilder.append("select\t").append(baseSelectFields).append("\r\n")
                .append("\t\tFROM\t").append(introspectedTable.getFullyQualifiedTableNameAtRuntime()).append("\r\n")
                .append("\t\tWHERE\t").append(introspectedTable.getPrimaryKeyColumns().get(0).getActualColumnName()).append("\r\n")
                .append("\t\t<foreach collection=\"list\" item=\"item\" open=\"IN (\" close=\")\" separator=\",\">\r\n")
                .append("\t\t\t").append("#{item.").append(introspectedTable.getPrimaryKeyColumns().get(0).getActualColumnName()).append("}").append("\r\n")
                .append("\t\t</foreach>");
        TextElement selectByIdsTextElement = new TextElement(sqlBuilder.toString());

        XmlElement selectByIds = new XmlElement("select");
        selectByIds.addAttribute(new Attribute("id", "selectByIds"));
        selectByIds.addAttribute(new Attribute("resultMap", "BaseResultMap"));
        selectByIds.addElement(selectByIdsTextElement);
        parentElement.addElement(selectByIds);
    }

    private void addSelectXmlElement(XmlElement parentElement) {
        StringBuilder sqlBuilder = new StringBuilder(100);
        String baseSelectFields = introspectedTable.getAllColumns().stream()
                .map(MyBatis3FormattingUtilities::getEscapedColumnName).collect(Collectors.joining(", "));
        sqlBuilder.append("select\t").append(baseSelectFields).append("\r\n")
                .append("\t\tFROM\t").append(introspectedTable.getFullyQualifiedTableNameAtRuntime()).append("\r\n")
                .append("\t\t<WHERE>").append("<trim suffixOverride=\",\">")
                .append("</trim>")
                .append("\t\t</WHERE>");
        TextElement selectByIdsTextElement = new TextElement(sqlBuilder.toString());

        XmlElement selectByIds = new XmlElement("select");
        selectByIds.addAttribute(new Attribute("id", "select"));
        selectByIds.addAttribute(new Attribute("resultMap", "BaseResultMap"));
        selectByIds.addElement(selectByIdsTextElement);
        parentElement.addElement(selectByIds);
    }

    private void addBatchUpdateById(XmlElement parentElement) {

    }

    private void addDeleteByIdsXmlElement(XmlElement parentElement) {

    }

    private void addDeleteXmlElement(XmlElement parentElement) {

    }

    private void addBatchInsertXmlElement(XmlElement parentElement) {

    }

    private void addBaseSelectFields(XmlElement parentElement) {
        XmlElement sql = new XmlElement("sql");
        sql.addAttribute(new Attribute("id", "baseSelectFields"));
        String baseSelectFields = introspectedTable.getAllColumns().stream()
                .map(MyBatis3FormattingUtilities::getEscapedColumnName).collect(Collectors.joining(", "));
        sql.addElement(new TextElement(baseSelectFields));
        parentElement.addElement(sql);
    }

    private void resetOriginXmlElement(XmlElement parentElement) {
        for (Element element : parentElement.getElements()) {
            for (Attribute attribute : ((XmlElement) element).getAttributes()) {
                if (attribute.getName().equalsIgnoreCase("id")
                        && attribute.getValue().equalsIgnoreCase("deleteByPrimaryKey")) {
                    ((XmlElement) element).addAttribute(new Attribute("id", "deleteById"));
                }
                if (attribute.getName().equalsIgnoreCase("id")
                        && attribute.getValue().equalsIgnoreCase("updateByPrimaryKey")) {
                    ((XmlElement) element).addAttribute(new Attribute("id", "updateById"));
                }
                if (attribute.getName().equalsIgnoreCase("id")
                        && attribute.getValue().equalsIgnoreCase("selectByPrimaryKey")) {
                    ((XmlElement) element).addAttribute(new Attribute("id", "selectById"));
                }
            }
        }
    }

    private XmlElement getIncludeElement() {
        XmlElement include = new XmlElement("include");
        include.addAttribute(new Attribute("refid", "baseSelectFields"));
        return include;
    }
}
