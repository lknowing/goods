package com.goods.business.service.impl;

import com.goods.business.mapper.ConsumerMapper;
import com.goods.business.service.ConsumerService;
import com.goods.common.model.business.Consumer;
import com.goods.common.utils.ListPageUtils;
import com.goods.common.vo.business.ConsumerVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * title:
 *
 * @Author xu
 * @Date 2022/09/25 21:25
 * @FileName: ConsumerServiceImpl
 */
@Service
public class ConsumerServiceImpl implements ConsumerService {
    @Autowired
    private ConsumerMapper consumerMapper;

    @Override
    public PageVO<ConsumerVO> findConsumerList(Integer pageNum, Integer pageSize, ConsumerVO consumerVO) {
        String name = consumerVO.getName();
        String address = consumerVO.getAddress();
        String contact = consumerVO.getContact();

        Example e = new Example(Consumer.class);
        Example.Criteria c = e.createCriteria();
        if (!StringUtils.isEmpty(name)) {
            c.andLike("name", "%" + name + "%");
        }
        if (!StringUtils.isEmpty(address)) {
            c.andLike("address", "%" + address + "%");
        }
        if (!StringUtils.isEmpty(contact)) {
            c.andLike("contact", "%" + contact + "%");
        }
        List<Consumer> consumers = consumerMapper.selectByExample(e);
        List<ConsumerVO> consumerVOList = consumers.stream().map(consumer -> {
            ConsumerVO vo = new ConsumerVO();
            BeanUtils.copyProperties(consumer, vo);
            return vo;
        }).collect(Collectors.toList());
        List<ConsumerVO> page = ListPageUtils.page(consumerVOList, pageSize, pageNum);
        return new PageVO<>(consumerVOList.size(), page);
    }

    @Override
    public void add(ConsumerVO consumerVO) {
        Consumer consumer = new Consumer();
        consumerVO.setCreateTime(new Date());
        consumerVO.setModifiedTime(new Date());
        BeanUtils.copyProperties(consumerVO, consumer);
        consumerMapper.insert(consumer);
    }

    @Override
    public ConsumerVO edit(Long consumerId) {
        Consumer consumer = consumerMapper.selectByPrimaryKey(consumerId);
        ConsumerVO consumerVO = new ConsumerVO();
        BeanUtils.copyProperties(consumer, consumerVO);
        return consumerVO;
    }

    @Override
    public void update(Long consumerId, ConsumerVO consumerVO) {
        Consumer consumer = new Consumer();
        consumerVO.setModifiedTime(new Date());
        BeanUtils.copyProperties(consumerVO, consumer);
        consumerMapper.updateByPrimaryKeySelective(consumer);
    }

    @Override
    public void delete(Long consumerId) {
        if (consumerId != null) {
            consumerMapper.deleteByPrimaryKey(consumerId);
        }
    }

    @Override
    public List<ConsumerVO> findAll() {
        List<Consumer> consumerList = consumerMapper.selectAll();
        List<ConsumerVO> consumerVOList = consumerList.stream().map(consumer -> {
            ConsumerVO consumerVO = new ConsumerVO();
            BeanUtils.copyProperties(consumer, consumerVO);
            return consumerVO;
        }).collect(Collectors.toList());
        return consumerVOList;
    }
}
