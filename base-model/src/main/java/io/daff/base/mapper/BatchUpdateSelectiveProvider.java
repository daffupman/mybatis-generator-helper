package io.daff.base.mapper;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Set;

/**
 * 批量更新的提供类
 *
 * @author daffupman
 * @since 2020/1/29
 */
public class BatchUpdateSelectiveProvider extends MapperTemplate {

    public BatchUpdateSelectiveProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 批量更新的执行逻辑
     *
     * <foreach collection="list" item="record" separator=";">
     *     update
     *          t_employee
     *     <set>
     *         <if test="emp_name != null">
     *              emp_name = #{record.empName},
     *         </if>
     *         <if test="emp_age != null">
     *              emp_age = #{record.empAge},
     *         </if>
     *         <if test="emp_salary != null">
     *              emp_salary = #{record.empSalary}
     *         </if>
     *
     *
     *     </set>
     *     where
     *          emp_id = #{record.empId}
     * </foreach>
     */
    public String batchUpdateSelective(MappedStatement ms) {

        // 获取实体类
        Class<?> entityClass = super.getEntityClass(ms);
        // 获取表名
        String tableName = super.tableName(entityClass);
        // 获取sql语句的update子句
        String updateClause = SqlHelper.updateTable(entityClass, tableName);

        // 获取实体类的属性
        Set<EntityColumn> columnSet = EntityHelper.getColumns(entityClass);
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
                setClause.append("<if test=\"record.").append(e.getProperty()).append(" != null\">").append("\n");
                setClause.append(column).append(" = ").append(columnHolder).append(",").append("\n");
                setClause.append("</if>").append("\n");
            }
        }

        // 拼接sql
        return  "<foreach collection=\"list\" item=\"record\" separator=\";\">" + "\n" +
                updateClause + "\n" +
                "<set>" + "\n" +
                setClause +
                "</set>" + "\n" +
                "WHERE " + idColumn + " = " + idHolder + "\n" +
                "</foreach>";
    }
}
