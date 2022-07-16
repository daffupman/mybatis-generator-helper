package io.daff.generator.support;

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
 * @author daff
 * @since 2020/10/31
 */
public class DefaultXmlElementGenerator extends AbstractXmlElementGenerator {

    @Override
    public void addElements(XmlElement parentElement) {

        overrideOriginXmlElement(parentElement);

        addBaseSelectFields(parentElement);
        addBatchInsertXmlElement(parentElement);
        addDeleteXmlElement(parentElement);
        addUpdateById(parentElement);
        addSelectById(parentElement);
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

    private void addSelectById(XmlElement parentElement) {
        IntrospectedColumn idIntrospectedColumn = introspectedTable.getPrimaryKeyColumns().get(0);
        String primaryKeyColumnName = idIntrospectedColumn.getActualColumnName();
        String primaryKeyJavaProperty = idIntrospectedColumn.getJavaProperty();
        String currTableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();

        String sqlBuilder = "select <include refid=\"baseColumns\"/>" + "\r\n" +
                "\tfrom\t" + currTableName + "\r\n" +
                "\twhere\t" + primaryKeyColumnName + " = #{" + primaryKeyJavaProperty + "}";
        TextElement selectByIdTextElement = new TextElement(sqlBuilder);

        XmlElement selectById = new XmlElement("select");
        selectById.addAttribute(new Attribute("id", "selectById"));
        selectById.addAttribute(new Attribute("resultMap", "BaseResultMap"));
        selectById.addElement(selectByIdTextElement);
        parentElement.addElement(selectById);
    }

    private void addUpdateById(XmlElement parentElement) {
        IntrospectedColumn idIntrospectedColumn = introspectedTable.getPrimaryKeyColumns().get(0);
        String primaryKeyColumnName = idIntrospectedColumn.getActualColumnName();
        String primaryKeyJavaProperty = idIntrospectedColumn.getJavaProperty();
        String currTableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
        StringBuilder updateClause = new StringBuilder();
        List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
        int size = allColumns.size();
        for (int i = 0; i < size; i++) {
            IntrospectedColumn column = allColumns.get(i);
            String columnName = MyBatis3FormattingUtilities.getEscapedColumnName(column);
            String javaProperty = column.getJavaProperty();
            updateClause.append("\t").append(columnName).append(" = #{").append(javaProperty).append("}");
            if (i != size - 1) {
                updateClause.append(",\r\n");
            }
        }

        String sqlBuilder = "update " + currTableName + " set\r\n" +
                updateClause + "\r\n" +
                "\twhere " + primaryKeyColumnName + " = #{" + primaryKeyJavaProperty + "}";
        TextElement updateTextElement = new TextElement(sqlBuilder);

        XmlElement updateById = new XmlElement("update");
        updateById.addAttribute(new Attribute("id", "updateById"));
        updateById.addElement(updateTextElement);
        parentElement.addElement(updateById);
    }

    private void addSelectByIds(XmlElement parentElement) {

        String primaryKeyColumnName = introspectedTable.getPrimaryKeyColumns().get(0).getActualColumnName();
        String currTableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();

        String sqlBuilder = "select <include refid=\"baseColumns\"/>" + "\r\n" +
                "\tfrom " + currTableName + "\r\n" +
                "\twhere " + primaryKeyColumnName + "\r\n" +
                "\t<foreach collection=\"list\" item=\"item\" open=\"in (\" close=\")\" separator=\",\">\r\n" +
                "\t\t" + "#{item." + primaryKeyColumnName + "}" + "\r\n" +
                "\t</foreach>";
        TextElement selectByIdsTextElement = new TextElement(sqlBuilder);

        XmlElement selectByIds = new XmlElement("select");
        selectByIds.addAttribute(new Attribute("id", "selectByIds"));
        selectByIds.addAttribute(new Attribute("resultMap", "BaseResultMap"));
        selectByIds.addElement(selectByIdsTextElement);
        parentElement.addElement(selectByIds);
    }

    private void addDeleteXmlElement(XmlElement parentElement) {

        String primaryKeyColumnName = introspectedTable.getPrimaryKeyColumns().get(0).getActualColumnName();
        String currTableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();

        String sqlBuilder = "delete from " + currTableName + "\r\n" +
                "\twhere id = #{" + primaryKeyColumnName+ "}";

        XmlElement batchInsertElement = new XmlElement("delete");
        batchInsertElement.addAttribute(new Attribute("id", "deleteById"));
        batchInsertElement.addElement(new TextElement(sqlBuilder));
        parentElement.addElement(batchInsertElement);
    }

    private void addBatchInsertXmlElement(XmlElement parentElement) {

        String currTableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
        String batchRecordStr = introspectedTable.getAllColumns().stream()
                .map(IntrospectedColumn::getJavaProperty).map(c -> "#{item." + c + "}").collect(Collectors.joining(", ", "(", ")"));

        String sqlBuilder = "insert into " + currTableName + "(<include refid=\"baseColumns\"/>)\r\n" +
                "\t<foreach collection=\"list\" item=\"item\" open=\"values \" separator=\",\">\r\n" +
                "\t\t" + batchRecordStr + "\r\n" +
                "\t</foreach>";

        XmlElement batchInsertElement = new XmlElement("insert");
        batchInsertElement.addAttribute(new Attribute("id", "batchInsert"));
        batchInsertElement.addElement(new TextElement(sqlBuilder));
        parentElement.addElement(batchInsertElement);
    }

    private void addBaseSelectFields(XmlElement parentElement) {
        XmlElement sql = new XmlElement("sql");
        sql.addAttribute(new Attribute("id", "baseColumns"));
        String baseSelectFields = introspectedTable.getAllColumns().stream()
                .map(MyBatis3FormattingUtilities::getEscapedColumnName).collect(Collectors.joining(", "));
        sql.addElement(new TextElement(baseSelectFields));
        parentElement.addElement(0, sql);
    }

    /**
     * 重写原始生成的sql xml
     */
    private void overrideOriginXmlElement(XmlElement parentElement) {
        Iterator<Element> elementIterator = parentElement.getElements().iterator();
        while (elementIterator.hasNext()) {

            Element element = elementIterator.next();
            boolean remove = false;
            for (Attribute attribute : ((XmlElement) element).getAttributes()) {
                // 删除带ByPrimaryKey的sql
                if (attribute.getName().equalsIgnoreCase("id") && attribute.getValue().contains("ByPrimaryKey")) {
                    remove = true;
                    break;
                }
            }
            if (remove) {
                elementIterator.remove();
            }
        }
    }
}
