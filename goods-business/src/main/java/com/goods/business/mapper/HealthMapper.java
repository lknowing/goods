package com.goods.business.mapper;

import com.goods.common.model.business.Health;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * title:
 *
 * @Author xu
 * @Date 2022/09/25 22:50
 * @FileName: HealthMapper
 */
@Repository
public interface HealthMapper extends Mapper<Health> {
}
