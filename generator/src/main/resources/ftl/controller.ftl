package ${topPackage}.controller;

import ${topPackage}.entity.${entityClassName};
import ${topPackage}.service.${entityClassName}Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ${entityInstanceName}的Controller层
 *
 * @author	${author}
 * @since	${date}
 */
@Api("${entityClassName}模块相关接口")
@RestController
@RequestMapping("/${entityInstanceName}")
public class ${entityClassName}Controller {

    @Autowired
    private ${entityClassName}Service ${entityInstanceName}Service;

    @ApiOperation(value = "新增记录")
    @PostMapping("/${entityInstanceName}")
    public int add${entityClassName}(
            @ApiParam(name = "${entityInstanceName}", value = "${entityClassName}对象", required = true)
            @RequestBody ${entityClassName} ${entityInstanceName}
    ) {

        return ${entityInstanceName}Service.save(${entityInstanceName});
    }

    @ApiOperation(value = "移除记录")
    @DeleteMapping("/${entityInstanceName}/{id}")
    public int remove${entityClassName}(
            @ApiParam(name = "id", value = "User对象的主键", required = true)
            @PathVariable("id")Integer id
    ) {

        return ${entityInstanceName}Service.removeById(id);
    }

    @ApiOperation(value = "修改记录")
    @PutMapping("/${entityInstanceName}")
    public int update${entityClassName}(
            @ApiParam(name = "${entityInstanceName}", value = "${entityInstanceName}对象", required = true)
            @RequestBody ${entityClassName} ${entityInstanceName}
    ) {

        return ${entityInstanceName}Service.updateById(user);
    }

    @ApiOperation(value = "获取所有记录")
    @GetMapping("/${entityInstanceName}")
    public List<${entityClassName}> getAll() {
        return ${entityInstanceName}Service.selectAll();
    }

}