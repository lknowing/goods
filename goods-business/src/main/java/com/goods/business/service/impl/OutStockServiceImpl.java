package com.goods.business.service.impl;

import com.alibaba.fastjson.JSON;
import com.goods.business.mapper.*;
import com.goods.business.service.OutStockService;
import com.goods.common.model.business.*;
import com.goods.common.response.ResponseBean;
import com.goods.common.utils.ListPageUtils;
import com.goods.common.vo.business.ConsumerVO;
import com.goods.common.vo.business.OutStockDetailVO;
import com.goods.common.vo.business.OutStockItemVO;
import com.goods.common.vo.business.OutStockVO;
import com.goods.common.vo.system.PageVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * title:
 *
 * @Author xu
 * @Date 2022/09/26 08:25
 * @FileName: OutStockServiceImpl
 */
@Service
public class OutStockServiceImpl implements OutStockService {
    @Autowired
    private OutStockMapper outStockMapper;

    @Autowired
    private ConsumerMapper consumerMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OutStockInfoMapper outStockInfoMapper;

    @Autowired
    private ProductStockMapper productStockMapper;

    @Override
    public PageVO<OutStockVO> findOutStockList(Integer pageNum, Integer pageSize, OutStockVO outStockVO) {
        Integer status = outStockVO.getStatus();
        Integer type = outStockVO.getType();
        String outNum = outStockVO.getOutNum();
        Example example = new Example(OutStock.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status", status);
        if (type != null) {
            criteria.andEqualTo("type", type);
        }
        if (outNum != null) {
            criteria.andLike("outNum", "%" + outNum + "%");
        }
        List<OutStock> outStocks = outStockMapper.selectByExample(example);
        List<OutStockVO> outStockVoList = outStocks.stream().map(outStock -> {
            OutStockVO outStockVo = new OutStockVO();
            BeanUtils.copyProperties(outStock, outStockVo);
            return outStockVo;
        }).collect(Collectors.toList());
        List<OutStockVO> page = ListPageUtils.page(outStockVoList, pageSize, pageNum);
        return new PageVO<>(outStockVoList.size(), page);
    }

    @Override
    public OutStockDetailVO detail(Long outStockId, Integer pageNum) {
        OutStock outStock = outStockMapper.selectByPrimaryKey(outStockId);
        Integer pageNo = (pageNum - 1) * 3;
        List<OutStockItemVO> outStockItemVOS = outStockMapper.getOutStockItemVO(outStock.getId(), pageNo);
        Consumer consumer = consumerMapper.selectByPrimaryKey(outStock.getConsumerId());
        ConsumerVO consumerVO = new ConsumerVO();
        BeanUtils.copyProperties(consumer, consumerVO);
        OutStockDetailVO outStockDetailVO = new OutStockDetailVO();
        outStockDetailVO.setStatus(outStock.getStatus());
        outStockDetailVO.setTotal(outStockItemVOS.size());
        outStockDetailVO.setItemVOS(outStockItemVOS);
        outStockDetailVO.setConsumerVO(consumerVO);
        return outStockDetailVO;
    }

    @Override
    public ResponseBean publish(Long outStockId) {
        OutStock outStock = outStockMapper.selectByPrimaryKey(outStockId);
        String outNum = outStock.getOutNum();
        Example o = new Example(InStockInfo.class);
        Example.Criteria c = o.createCriteria();
        c.andEqualTo("outNum", outNum);
        List<OutStockInfo> outStockInfos = outStockInfoMapper.selectByExample(o);
        ArrayList<String> errorList = new ArrayList<>();
        outStockInfos.forEach(outStockInfo -> {
            String pNum = outStockInfo.getPNum();
            Integer productNumber = outStockInfo.getProductNumber();
            Example example = new Example(ProductStock.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("pNum", pNum);
            ProductStock productStock = productStockMapper.selectOneByExample(example);
            Long stockStock = productStock.getStock();
            if (stockStock >= productNumber) {
                stockStock -= productNumber;
                productStockMapper.updateByPNum(pNum, stockStock);
            } else {
                Example e = new Example(Product.class);
                Example.Criteria cr = e.createCriteria();
                cr.andEqualTo("pNum", pNum);
                Product product = productMapper.selectOneByExample(e);
                long num = productNumber - stockStock;
                errorList.add(product.getName() + "库存少了" + num + "件");
                stockStock = 0L;
                productStockMapper.updateByPNum(pNum, stockStock);
                outStockInfo.setProductNumber(stockStock.intValue());
                outStockInfoMapper.updateByPrimaryKeySelective(outStockInfo);
            }
        });
        outStock.setStatus(0);
        outStockMapper.updateByPrimaryKeySelective(outStock);
        if (errorList.size() > 0) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("errorMsg", StringUtils.join(errorList, ","));
            return ResponseBean.error(map);
        }
        return ResponseBean.success();
    }

    @Override
    public void back(Long outStockId) {
        OutStock outStock = new OutStock();
        outStock.setId(outStockId);
        outStock.setStatus(0);
        outStockMapper.updateByPrimaryKeySelective(outStock);
    }

    @Override
    public void delete(Long outStockId) {
        outStockMapper.deleteByPrimaryKey(outStockId);
        OutStock outStock = outStockMapper.selectByPrimaryKey(outStockId);
        String outNum = outStock.getOutNum();
        Example example = new Example(OutStockInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("outNum", outNum);
        outStockMapper.deleteByExample(example);
    }

    @Override
    public void remove(Long outStockId) {
        OutStock outStock = new OutStock();
        outStock.setId(outStockId);
        outStock.setStatus(1);
        outStockMapper.updateByPrimaryKeySelective(outStock);
    }

    @Override
    public void addIntoStock(OutStockVO outStockVO) {
        outStockVO.setCreateTime(new Date());
        OutStock outStock = new OutStock();
        BeanUtils.copyProperties(outStockVO, outStock);
        if (outStockVO.getConsumerId() == null) {
            ConsumerVO consumerVO = new ConsumerVO();
            consumerVO.setCreateTime(new Date());
            consumerVO.setModifiedTime(new Date());
            consumerVO.setName(outStockVO.getName());
            consumerVO.setAddress(outStockVO.getAddress());
            consumerVO.setPhone(outStockVO.getPhone());
            consumerVO.setSort(outStockVO.getSort());
            consumerVO.setContact(outStockVO.getContact());
            Consumer consumer = new Consumer();
            BeanUtils.copyProperties(consumerVO, consumer);
            consumerMapper.insert(consumer);
            outStock.setConsumerId(consumer.getId());
        }
        outStock.setStatus(2);
        List<Object> products = outStockVO.getProducts();
        String jsonString = JSON.toJSONString(products);
        List<Map> productsList = JSON.parseArray(jsonString, Map.class);

        AtomicReference<Integer> productNum = new AtomicReference<>(0);
        outStock.setOutNum(UUID.randomUUID().toString().replaceAll("-", ""));
        productsList.forEach(map -> {
            Integer productId = (Integer) map.get("productId");
            Integer productNumber = (Integer) map.get("productNumber");
            productNum.updateAndGet(v -> v + productNumber);
            String pNum = productMapper.selectByPrimaryKey(productId).getPNum();
            OutStockInfo outStockInfo = new OutStockInfo();
            outStockInfo.setOutNum(outStock.getOutNum());
            outStockInfo.setPNum(pNum);
            outStockInfo.setProductNumber(productNumber);
            outStockInfo.setCreateTime(new Date());
            outStockInfo.setModifiedTime(new Date());
            outStockInfoMapper.insert(outStockInfo);
        });
        outStock.setProductNumber(productNum.get());

        outStockMapper.insert(outStock);
    }
}
