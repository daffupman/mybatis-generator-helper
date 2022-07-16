package io.daff.base.mapper;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 批量更新的提供类
 *
 * @author daff
 * @since 2020/1/29
 */
public class BatchUpdateSelectiveProvider extends MapperTemplate {

    public BatchUpdateSelectiveProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 批量更新的执行逻辑
     *
     *     update
     *          t_employee
     *     <trim prefix="set" suffixOverrides=",">
     *         <trim prefix="emp_name = case" suffix="end,">
     *             <foreach collection="list" item="item" index="index">
     *                 <if test="item.emp_name != null">
     *                     when id = #{item.id} then #{item.empName}
     *                 </if>
     *             </foreach>
     *         </trim>
     *         <trim prefix="emp_age = case" suffix="end,">
     *             <foreach collection="list" item="item" index="index">
     *                 <if test="item.emp_age != null">
     *                     when id = #{item.id} then #{item.empAge}
     *                 </if>
     *             </foreach>
     *         </trim>
     *         <trim prefix="emp_salary = case" suffix="end,">
     *             <foreach collection="list" item="item" index="index">
     *                 <if test="item.emp_salary != null">
     *                     when id = #{item.id} then #{item.empSalary}
     *                 </if>
     *             </foreach>
     *         </trim>
     *     </trim>
     *     WHERE id in
     *     <foreach collection="list" item="item" open="(" close=")">
     *         #{item.id}
     *     </foreach>
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
        StringBuilder trimClause = new StringBuilder(1024);

        List<EntityColumn> idColumnList = columnSet.stream().filter(EntityColumn::isId).collect(Collectors.toList());
        if (idColumnList.size() != 1) {
            throw new IllegalArgumentException("class:" + entityClass + ", must has one id");
        }
        // 当前列为主键，记录相关信息拼接where子句
        String idColumn = idColumnList.get(0).getColumn();
        String idProperty = idColumnList.get(0).getProperty();

        for (EntityColumn e : columnSet) {
            if (!e.isId()) {
                // 否则拼接到set子句中
                String column = e.getColumn();
                String property = e.getProperty();

                trimClause.append("<trim prefix=\"").append(column).append(" = case\" suffix=\"end,\">").append("\n");
                trimClause.append("<foreach collection=\"list\" item=\"item\">").append("\n");
                trimClause.append("<if test=\"item.").append(property).append(" != null\">").append("\n");
                trimClause.append("when ").append(idColumn).append(" = #{item.").append(idProperty).append("} then #{item.").append(property).append("}").append("\n");
                trimClause.append("</if>").append("\n");
                trimClause.append("</foreach>").append("\n");
                trimClause.append("</trim>").append("\n");
            }
        }

        String whereClause = " WHERE " + idColumn + " IN " +
                "<foreach collection=\"list\" item=\"item\" open=\"(\" close=\")\" separator=\",\">" +
                "#{item." + idProperty + "}" +
                "</foreach>";

        // 拼接sql
        return  updateClause +
                "<trim prefix=\"set\" suffixOverrides=\",\">" + "\n" +
                trimClause +
                "</trim>" + "\n" +
                whereClause;
    }
}
