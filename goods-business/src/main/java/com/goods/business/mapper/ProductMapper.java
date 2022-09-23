package com.goods.business.mapper;

import com.goods.common.model.business.Product;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * title:
 *
 * @Author xu
 * @Date 2022/09/23 21:00
 * @FileName: ProductMapper
 */
@Repository
public interface ProductMapper extends Mapper<Product> {
}
