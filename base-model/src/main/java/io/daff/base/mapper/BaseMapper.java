package io.daff.base.mapper;

import tk.mybatis.mapper.common.Mapper;

/**
 * 自定义的mapper接口，供通用mapper生成器使用
 *
 * @author daffupman
 * @since 2020/1/30
 */
public interface BaseMapper<T>
        extends Mapper<T>, BatchUpdateSelectiveMapper<T>{
}
