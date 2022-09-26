package com.goods.business.mapper;

import com.goods.common.model.business.OutStock;
import com.goods.common.vo.business.OutStockItemVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * title:
 *
 * @Author xu
 * @Date 2022/09/26 08:26
 * @FileName: OutStockMapper
 */
@Repository
public interface OutStockMapper extends Mapper<OutStock> {
    List<OutStockItemVO> getOutStockItemVO(@Param("outStockId") Long id, @Param("pageNo") Integer pageNo);
}
