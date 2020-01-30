package ${topPackage}.service;

import ${topPackage}.entity.${entityClassName};
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * ${entityInstanceName}的Service层接口
 *
 * @author	${author}
 * @since	${date}
 */
public interface ${entityClassName}Service {

    /**
    * 新增记录
    */
    int save(${entityClassName} ${entityInstanceName});

    /**
    * 按主键删除记录
    */
    int removeById(Integer id);

    /**
    * 按example删除记录
    */
    int removeByExample(${entityClassName} ${entityInstanceName});

    /**
    * 按主键更新记录
    */
    int updateById(${entityClassName} ${entityInstanceName});

    /**
    * 按example更新记录
    */
    int updateByExample(${entityClassName} ${entityInstanceName}, Example example);

    /**
    * 按主键查询记录
    */
    User selectById(Integer id);

    /**
    * 查询所有记录
    */
    List<${entityClassName}> selectAll();

    /**
    * 按example查询记录
    */
    List<${entityClassName}> selectByExample(Example example);

}