package com.goods.business.mapper;

import com.goods.common.model.business.InStock;
import com.goods.common.vo.business.InStockItemVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * title:
 *
 * @Author xu
 * @Date 2022/09/24 13:53
 * @FileName: InStockMapper
 */
@Repository
public interface InStockMapper extends Mapper<InStock> {
    List<InStockItemVO> getInStockItemVO(@Param("inStockId") Long id, @Param("pageNo") Integer pageNo);
}
