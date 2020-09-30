package io.daff.base.mapper;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.UpdateProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

/**
 * 拓展mapper接口
 *
 * @author daffupman
 * @since 2020/1/29
 */
@RegisterMapper
public interface UpdateByPrimaryKeyWithoutIgnoreFieldsMapper<T> {

    @Options(useGeneratedKeys = true)
    @UpdateProvider(type = UpdateByPrimaryKeyWithoutIgnoreFieldsProvider.class, method = "dynamicSQL")
    int updateByPrimaryKeyWithoutIgnoreFields(List<T> list);
}
