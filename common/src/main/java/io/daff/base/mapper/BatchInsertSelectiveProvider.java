package io.daff.base.mapper;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Set;

/**
 * 批量插入的提供类
 *
 * @author daff
 * @since 2020/1/30
 */
public class BatchInsertSelectiveProvider extends MapperTemplate {

    public BatchInsertSelectiveProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 拼接批量插入的sql语句
     *
     * <foreach collection="list" item="record" separator=";">
     *      insert into t_employee
     *      <trim prefix="(" suffix=")" suffixOverrides=",">
     *          <if test="record.emp_name != null">emp_name,</if>
     *          <if test="record.emp_age != null">emp_age,</if>
     *          <if test="record.emp_salary != null">emp_salary,</if>
     *      </trim>
     *      VALUES
     *      <trim prefix="(" suffix=")" suffixOverrides=",">
     *          <if test="record.emp_name != null">
     *              #{record.empName},
     *          </if>
     *          <if test="record.emp_age != null">
     *              #{record.empAge},
     *          </if>
     *          <if test="record.emp_salary != null">
     *              #{record.empSalary}
     *          </if>
     *      </trim>
     * </foreach>
     */
    public String batchInsertSelective(MappedStatement ms) {

        // 获取实体类
        Class<?> entityClass = super.getEntityClass(ms);
        // 获取表名
        String tableName = super.tableName(entityClass);
        // 获取sql语句的insert子句
        String insertClause = SqlHelper.insertIntoTable(entityClass, tableName);

        // 获取实体类的属性
        Set<EntityColumn> columnSet = EntityHelper.getColumns(entityClass);
        // insert子句
        StringBuilder fieldClause = new StringBuilder(1024);
        StringBuilder valueClause = new StringBuilder(1024);
        for (EntityColumn e : columnSet) {
            String column = e.getColumn();
            String property = e.getProperty();
            String columnHolder = e.getColumnHolder("record");

            fieldClause.append("<if test=\"record.").append(property).append(" != null\">").append(column).append(",").append("</if>").append("\n");

            valueClause.append("<if test=\"record.").append(property).append(" != null\">").append("\n");
            valueClause.append(columnHolder).append(",").append("\n");
            valueClause.append("</if>").append("\n");
        }

        // 拼接sql
        return insertClause + "\n" +
                "<foreach collection=\"list\" item=\"record\" index=\"index\">" + "\n" +
                "<if test=\"index == 0\"><trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">" + "\n" +
                fieldClause +
                "</trim></if>" +
                "</foreach>" +
                "VALUES" + "\n" +
                "<foreach collection=\"list\" item=\"record\" separator=\",\">" + "\n" +
                "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">" + "\n" +
                valueClause +
                "</trim>" + "\n" +
                "</foreach>";
    }

}
