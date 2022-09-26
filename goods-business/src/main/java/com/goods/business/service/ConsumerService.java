package com.goods.business.service;

import com.goods.common.vo.business.ConsumerVO;
import com.goods.common.vo.system.PageVO;

import java.util.List;

/**
 * title:
 *
 * @Author xu
 * @Date 2022/09/25 21:25
 * @FileName: ConsumerService
 */
public interface ConsumerService {
    PageVO<ConsumerVO> findConsumerList(Integer pageNum, Integer pageSize, ConsumerVO consumerVO);

    void add(ConsumerVO consumerVO);

    ConsumerVO edit(Long consumerId);

    void update(Long consumerId, ConsumerVO consumerVO);

    void delete(Long consumerId);

    List<ConsumerVO> findAll();
}
