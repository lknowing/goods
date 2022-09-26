package com.goods.business.service;

import com.goods.common.vo.business.InStockDetailVO;
import com.goods.common.vo.business.InStockVO;
import com.goods.common.vo.system.PageVO;

import java.util.Map;

/**
 * title:
 *
 * @Author xu
 * @Date 2022/09/24 13:43
 * @FileName: InStockService
 */
public interface InStockService {
    PageVO<InStockVO> findInStockList(Integer pageNum, Integer pageSize, InStockVO inStockVO);

    void remove(Long inStockId);

    void delete(Long inStockId);

    void back(Long inStockId);

    InStockDetailVO detail(Long inStockId, Integer pageNum);

    void addIntoStock(InStockVO inStockVO);

    void publish(Long inStockId);
}
