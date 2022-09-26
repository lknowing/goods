package com.goods.business.service;

import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.OutStockDetailVO;
import com.goods.common.vo.business.OutStockVO;
import com.goods.common.vo.system.PageVO;

import java.util.Map;

/**
 * title:
 *
 * @Author xu
 * @Date 2022/09/26 08:24
 * @FileName: OutStockService
 */
public interface OutStockService {
    PageVO<OutStockVO> findOutStockList(Integer pageNum, Integer pageSize, OutStockVO outStockVO);

    OutStockDetailVO detail(Long outStockId, Integer pageNum);

    ResponseBean publish(Long outStockId);

    void back(Long outStockId);

    void delete(Long outStockId);

    void remove(Long outStockId);

    void addIntoStock(OutStockVO outStockVO);
}
