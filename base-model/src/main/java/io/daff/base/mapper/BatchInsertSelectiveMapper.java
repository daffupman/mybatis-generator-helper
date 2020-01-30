package io.daff.base.mapper;

import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

/**
 * 批量插入mapper接口
 *
 * @author daffupman
 * @since 2020/1/30
 */
public interface BatchInsertSelectiveMapper<T> {

    @UpdateProvider(type = BatchInsertSelectiveProvider.class, method = "dynamicSQL")
    void batchInsertSelective(List<T> list);
}
