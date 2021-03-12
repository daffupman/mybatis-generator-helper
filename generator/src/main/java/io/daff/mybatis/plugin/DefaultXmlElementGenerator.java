package io.daff.mybatis.plugin;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.Arrays;
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
        addDeleteByIdsXmlElement(parentElement);
        addSelectXmlElement(parentElement);
        addSelectByIds(parentElement);
        addBatchUpdateXmlElement(parentElement);
    }

    /**
     * 自动生成根据id列表查询的功能
     */
    private void addSelectByIds(XmlElement parentElement) {

        String sqlBuilder = "select\t" + "<include refid=\"baseSelectFields\"/>" + "\r\n" +
                "\tfrom " + introspectedTable.getFullyQualifiedTableNameAtRuntime() + "\r\n" +
                "\twhere " + introspectedTable.getPrimaryKeyColumns().get(0).getActualColumnName() + "\r\n" +
                "\t\t<foreach collection=\"collection\" item=\"item\" open=\"in (\" close=\")\" separator=\",\">\r\n" +
                "\t\t\t" + "#{item}" + "\r\n" +
                "\t\t</foreach>";
        TextElement selectByIdsTextElement = new TextElement(sqlBuilder);

        XmlElement selectByIds = new XmlElement("select");
        selectByIds.addAttribute(new Attribute("id", "selectByIds"));
        selectByIds.addAttribute(new Attribute("resultMap", "BaseResultMap"));
        selectByIds.addElement(selectByIdsTextElement);
        parentElement.addElement(selectByIds);
    }

    /**
     * 自动生成按条件查询记录的功能
     */
    private void addSelectXmlElement(XmlElement parentElement) {
        StringBuilder sqlBuilder = new StringBuilder(1024);

        List<EntityProperty> javaEntityPropertyList = introspectedTable.getBaseColumns().stream()
                .map(each -> new EntityProperty(each.getJavaProperty(), each.getFullyQualifiedJavaType().getShortName(), each.getActualColumnName()))
                .collect(Collectors.toList());

        // 构造where的条件语句
        StringBuilder ifBuilder = new StringBuilder(256);
        javaEntityPropertyList.forEach(each -> {
            StringBuilder ifItemBuilder = new StringBuilder(128);
            String propertyName = each.getPropertyName();
            String testCondition = propertyName + " != null";
            if (each.getJavaShortPropertyType().equalsIgnoreCase("string")) {
                testCondition += " and " + propertyName + " != ''";
            }
            ifItemBuilder.append("\t\t<if test=\"").append(testCondition).append("\">\n")
                    .append("\t\t  ").append(each.getColumnName()).append(" = ").append("#{").append(each.getPropertyName()).append("}").append("\n")
                    .append("\t\t</if>\n");

            ifBuilder.append(ifItemBuilder);
        });

        sqlBuilder.append("select ").append("<include refid=\"baseSelectFields\"/>").append("\r\n")
                .append("\tfrom ").append(introspectedTable.getFullyQualifiedTableNameAtRuntime()).append("\r\n")
                .append("\t<where>\n")
                .append("\t  <trim suffixOverrides=\",\">\n")
                .append(ifBuilder)
                .append("\t  </trim>\n")
                .append("\t</where>");
        TextElement selectByIdsTextElement = new TextElement(sqlBuilder.toString());

        XmlElement select = new XmlElement("select");
        select.addAttribute(new Attribute("id", "select"));
        select.addAttribute(new Attribute("resultMap", "BaseResultMap"));
        select.addElement(selectByIdsTextElement);
        parentElement.addElement(select);
    }

    /**
     * 自动生成按id列表删除的功能
     */
    private void addDeleteByIdsXmlElement(XmlElement parentElement) {
        String sqlBuilder = "delete from " + introspectedTable.getFullyQualifiedTableNameAtRuntime() + "\r\n" +
                "\twhere " + introspectedTable.getPrimaryKeyColumns().get(0).getActualColumnName() + "\r\n" +
                "\t  <foreach collection=\"collection\" item=\"item\" open=\"in (\" close=\")\" separator=\",\">\r\n" +
                "\t\t" + "#{item}" + "\r\n" +
                "\t  </foreach>";
        TextElement deleteByIdsTextElement = new TextElement(sqlBuilder);

        XmlElement deleteByIds = new XmlElement("delete");
        deleteByIds.addAttribute(new Attribute("id", "deleteByIds"));
        deleteByIds.addElement(deleteByIdsTextElement);
        parentElement.addElement(deleteByIds);
    }

    /**
     * 自动生成批量新增的功能
     */
    private void addBatchInsertXmlElement(XmlElement parentElement) {
        StringBuilder sqlBuilder = new StringBuilder();

        StringBuilder columnBuilder = new StringBuilder(128);
        StringBuilder valueBuilder = new StringBuilder(512);
        List<EntityProperty> javaEntityPropertyList = introspectedTable.getAllColumns().stream()
                .map(each -> new EntityProperty(each.getJavaProperty(), each.getFullyQualifiedJavaType().getShortName(), each.getActualColumnName()))
                .collect(Collectors.toList());

        javaEntityPropertyList.forEach(each -> {
            StringBuilder columnItemBuilder = new StringBuilder(128);
            StringBuilder valueItemBuilder = new StringBuilder(128);
            String propertyName = each.getPropertyName();
            columnItemBuilder.append("\t\t<if test=\"").append(getTestCondition("collection[0]", propertyName))
                    .append("\">").append(each.getColumnName()).append(",").append("</if>\n");
            valueItemBuilder.append("\t\t<if test=\"").append(getTestCondition("item", propertyName)).append("\">")
                    .append("#{item.").append(each.getPropertyName()).append("}").append(",").append("</if>\n");

            columnBuilder.append(columnItemBuilder);
            valueBuilder.append(valueItemBuilder);
        });

        sqlBuilder.append("insert into ").append(introspectedTable.getFullyQualifiedTableNameAtRuntime()).append("(").append("\n")
                .append("\t  <trim suffixOverrides=\",\">\n")
                .append(columnBuilder)
                .append("\t  </trim>\n")
                .append("\t)\n")
                .append("\tvalues").append("\n")
                .append("\t<foreach collection=\"list\" item=\"item\" separator=\",\">\n")
                .append("\t(\n")
                .append("\t  <trim suffixOverrides=\",\">\n")
                .append(valueBuilder)
                .append("\t  </trim>\n")
                .append("\t)\n")
                .append("\t</foreach>");

        TextElement batchInsertTextElement = new TextElement(sqlBuilder.toString());
        XmlElement batchInsert = new XmlElement("insert");
        batchInsert.addAttribute(new Attribute("id", "batchInsert"));
        batchInsert.addAttribute(new Attribute("useGeneratedKeys", "true"));
        batchInsert.addAttribute(new Attribute("keyProperty", introspectedTable.getPrimaryKeyColumns().get(0).getJavaProperty()));
        batchInsert.addElement(batchInsertTextElement);
        parentElement.addElement(batchInsert);
    }

    /**
     * 自动生成批量新增的功能
     */
    private void addBatchUpdateXmlElement(XmlElement parentElement) {
        StringBuilder sqlBuilder = new StringBuilder();

        StringBuilder setClauseBuilder = new StringBuilder(128);
        List<IntrospectedColumn> baseColumns = introspectedTable.getBaseColumns();
        baseColumns.addAll(introspectedTable.getBLOBColumns());
        List<EntityProperty> javaEntityPropertyList = baseColumns.stream()
                .map(each -> new EntityProperty(each.getJavaProperty(), each.getFullyQualifiedJavaType().getShortName(), each.getActualColumnName()))
                .collect(Collectors.toList());

        javaEntityPropertyList.forEach(each -> {
            StringBuilder columnItemBuilder = new StringBuilder(128);
            String propertyName = each.getPropertyName();
            columnItemBuilder.append("\t    <if test=\"").append(getTestCondition("collection[0]", propertyName)).append("\">")
                    .append(each.getColumnName()).append(" = ").append("#{item.").append(each.getPropertyName()).append("}").append(",")
                    .append("</if>\n");

            setClauseBuilder.append(columnItemBuilder);
        });

        sqlBuilder.append("<foreach collection=\"list\" item=\"item\" separator=\",\">\n")
                .append("\t\t\tupdate ").append(introspectedTable.getFullyQualifiedTableNameAtRuntime()).append("\n")
                .append("\t\t\tset\n")
                .append("\t\t\t<trim suffixOverrides=\",\">\n")
                .append(setClauseBuilder)
                .append("\t\t\t</trim>\n")
                .append("\t\t\twhere ").append(introspectedTable.getPrimaryKeyColumns().get(0).getActualColumnName()).append(" = ").append("#{item.").append(introspectedTable.getPrimaryKeyColumns().get(0).getJavaProperty()).append("}").append("\n")
                .append("\t\t</foreach>");

        TextElement batchUpdateTextElement = new TextElement(sqlBuilder.toString());
        XmlElement batchUpdate = new XmlElement("update");
        batchUpdate.addAttribute(new Attribute("id", "batchUpdate"));
        batchUpdate.addElement(batchUpdateTextElement);
        parentElement.addElement(batchUpdate);
    }

    private String getTestCondition(String prefix, String propertyName) {
        return prefix + "." + propertyName + " != null";
    }

    /**
     * 自动生成表的基础字段
     */
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
