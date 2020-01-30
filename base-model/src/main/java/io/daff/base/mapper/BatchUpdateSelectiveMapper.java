package io.daff.base.mapper;

import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

/**
 * 拓展mapper接口
 *
 * @author daffupman
 * @since 2020/1/29
 */
public interface BatchUpdateSelectiveMapper<T> {

    @UpdateProvider(type = BatchUpdateSelectiveProvider.class, method = "dynamicSQL")
    void batchUpdateSelective(List<T> list);
}
