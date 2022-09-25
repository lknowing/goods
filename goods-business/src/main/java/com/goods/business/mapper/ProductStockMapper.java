package com.goods.business.mapper;

import com.goods.common.model.business.ProductStock;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * title:
 *
 * @Author xu
 * @Date 2022/09/24 21:17
 * @FileName: ProductStockMapper
 */
@Repository
public interface ProductStockMapper extends Mapper<ProductStock> {
    void updateByPNum(@Param("pNum") String pNum, @Param("stock") Long stock);
}
