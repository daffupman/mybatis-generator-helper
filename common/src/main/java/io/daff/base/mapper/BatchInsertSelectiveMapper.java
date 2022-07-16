package io.daff.base.mapper;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

/**
 * 批量插入mapper接口
 *
 * @author daff
 * @since 2020/1/30
 */
@RegisterMapper
public interface BatchInsertSelectiveMapper<T> {

    @Options(useGeneratedKeys = true)
    @InsertProvider(type = BatchInsertSelectiveProvider.class, method = "dynamicSQL")
    int batchInsertSelective(List<T> list);
}
