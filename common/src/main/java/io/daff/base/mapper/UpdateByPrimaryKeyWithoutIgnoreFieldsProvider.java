package io.daff.base.mapper;

import io.daff.base.annotation.IgnoreField;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 更新操作，可忽略一些字段
 *
 * @author daff
 * @since 2020/9/30
 */
public class UpdateByPrimaryKeyWithoutIgnoreFieldsProvider extends MapperTemplate {

    public UpdateByPrimaryKeyWithoutIgnoreFieldsProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 可忽略更新语句
     */
    public String updateByPrimaryKeyWithoutIgnoreFields(MappedStatement ms) {

        // 获取实体类
        Class<?> entityClass = super.getEntityClass(ms);
        // 获取表名
        String tableName = super.tableName(entityClass);
        // 获取sql语句的update子句
        String updateClause = SqlHelper.updateTable(entityClass, tableName);

        // 获取实体类的属性
        Set<EntityColumn> columnSet = getColumnsWithoutIgnoreFields(entityClass);
        // set子句
        StringBuilder setClause = new StringBuilder(1024);
        String idColumn = null;
        String idHolder = null;
        for (EntityColumn e : columnSet) {
            if (e.isId()) {
                // 当前列为主键，记录相关信息拼接where子句
                idColumn = e.getColumn();
                idHolder = e.getColumnHolder("record");
            } else {
                // 否则拼接到set子句中
                String column = e.getColumn();
                String columnHolder = e.getColumnHolder("record");
                setClause.append(column).append(" = ").append(columnHolder).append(",").append("\n");
            }
        }

        // 拼接sql
        return "<foreach collection=\"list\" item=\"record\" separator=\";\">" + "\n" +
                updateClause + "\n" +
                "SET" + "\n" +
                setClause.substring(0, setClause.lastIndexOf(",")) +
                "WHERE " + idColumn + " = " + idHolder + "\n" +
                "</foreach>";
    }

    /**
     * 获取没有标注IgnoreField注解的列
     */
    private Set<EntityColumn> getColumnsWithoutIgnoreFields(Class<?> entityClass) {

        return EntityHelper.getColumns(entityClass).stream()
                .map(each -> !each.getEntityField().isAnnotationPresent(IgnoreField.class) ? each : null)
                .filter(Objects::nonNull).collect(Collectors.toSet());
    }
}
