package ${topPackage}.service.impl;

import ${topPackage}.entity.${entityClassName};
import ${topPackage}.mapper.${entityClassName}Mapper;
import ${topPackage}.service.${entityClassName}Service;
import tk.mybatis.mapper.entity.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ${entityInstanceName}的Service层实现类
 *
 * @author	${author}
 * @since	${date}
 */
@Service
public class ${entityClassName}ServiceImpl implements ${entityClassName}Service {

    @Autowired
    private ${entityClassName}Mapper ${entityInstanceName}Mapper;

    @Override
    public int save(${entityClassName} ${entityInstanceName}) {
        return ${entityInstanceName}Mapper.insertSelective(${entityInstanceName});
    }

    @Override
    public int removeById(Integer id) {
        return ${entityInstanceName}Mapper.deleteByPrimaryKey(id);
    }

    @Override
    public int removeByExample(${entityClassName} ${entityInstanceName}) {
        return ${entityInstanceName}Mapper.deleteByExample(${entityInstanceName});
    }

    @Override
    public int updateById(${entityClassName} ${entityInstanceName}) {
        return ${entityInstanceName}Mapper.updateByPrimaryKeySelective(${entityInstanceName});
    }

    @Override
    public int updateByExample(${entityClassName} ${entityInstanceName}, Example example) {
        return ${entityInstanceName}Mapper.updateByExampleSelective(${entityInstanceName}, example);
    }

    @Override
    public User selectById(Integer id) {
        return ${entityInstanceName}Mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<${entityClassName}> selectAll() {
        return ${entityInstanceName}Mapper.selectAll();
    }

    @Override
    public List<${entityClassName}> selectByExample(Example example) {
        return ${entityInstanceName}Mapper.selectByExample(example);
    }

}
