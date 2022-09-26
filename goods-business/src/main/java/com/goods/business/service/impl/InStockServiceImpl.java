package com.goods.business.service.impl;

import com.alibaba.fastjson.JSON;
import com.goods.business.mapper.*;
import com.goods.business.service.InStockService;
import com.goods.common.model.business.InStock;
import com.goods.common.model.business.InStockInfo;
import com.goods.common.model.business.ProductStock;
import com.goods.common.model.business.Supplier;
import com.goods.common.utils.ListPageUtils;
import com.goods.common.vo.business.InStockDetailVO;
import com.goods.common.vo.business.InStockItemVO;
import com.goods.common.vo.business.InStockVO;
import com.goods.common.vo.business.SupplierVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * title:
 *
 * @Author xu
 * @Date 2022/09/24 13:44
 * @FileName: InStockServiceImpl
 */
@Service
public class InStockServiceImpl implements InStockService {
    @Autowired
    private InStockMapper inStockMapper;

    @Autowired
    private SupplyMapper supplyMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private InStockInfoMapper inStockInfoMapper;

    @Autowired
    private ProductStockMapper productStockMapper;

    @Override
    public PageVO<InStockVO> findInStockList(Integer pageNum, Integer pageSize, InStockVO inStockVO) {
        Integer type = inStockVO.getType();
        Integer status = inStockVO.getStatus();
        String inNum = inStockVO.getInNum();
        Date startTime = inStockVO.getStartTime();
        Date endTime = inStockVO.getEndTime();
        Example example = new Example(InStock.class);
        Example.Criteria criteria = example.createCriteria();
        if (type != null) {
            criteria.andEqualTo("type", type);
        }
        if (status != null) {
            criteria.andEqualTo("status", status);
        }
        if (inNum != null) {
            criteria.andLike("inNum", "%" + inNum + "%");
        }
        if (startTime != null && endTime != null) {
            criteria.andBetween("createTime", startTime, endTime);
        }
        List<InStock> inStocks = inStockMapper.selectByExample(example);
        List<InStockVO> inStockVoList = inStocks.stream().map(inStock -> {
            InStockVO inStockVo = new InStockVO();
            BeanUtils.copyProperties(inStock, inStockVo);
            Supplier supplier = supplyMapper.selectByPrimaryKey(inStock.getSupplierId());
            inStockVo.setSupplierName(supplier.getName());
            inStockVo.setPhone(supplier.getPhone());
            return inStockVo;
        }).collect(Collectors.toList());
        List<InStockVO> page = ListPageUtils.page(inStockVoList, pageSize, pageNum);

        return new PageVO<>(inStockVoList.size(), page);
    }

    @Override
    public void remove(Long inStockId) {
        InStock inStock = new InStock();
        inStock.setId(inStockId);
        inStock.setStatus(1);
        inStockMapper.updateByPrimaryKeySelective(inStock);
    }

    @Override
    public void delete(Long inStockId) {
        inStockMapper.deleteByPrimaryKey(inStockId);
        InStock inStock = inStockMapper.selectByPrimaryKey(inStockId);
        String inNum = inStock.getInNum();
        Example example = new Example(InStockInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("inNum", inNum);
        inStockInfoMapper.deleteByExample(example);
    }

    @Override
    public void back(Long inStockId) {
        InStock inStock = new InStock();
        inStock.setId(inStockId);
        inStock.setStatus(0);
        inStockMapper.updateByPrimaryKeySelective(inStock);
    }

    @Override
    public InStockDetailVO detail(Long inStockId, Integer pageNum) {
        InStock inStock = inStockMapper.selectByPrimaryKey(inStockId);
        Integer pageNo = (pageNum - 1) * 3;
        List<InStockItemVO> inStockItemVOS = inStockMapper.getInStockItemVO(inStock.getId(), pageNo);
        Supplier supplier = supplyMapper.selectByPrimaryKey(inStock.getSupplierId());
        SupplierVO supplierVO = new SupplierVO();
        BeanUtils.copyProperties(supplier, supplierVO);
        InStockDetailVO inStockDetailVO = new InStockDetailVO();
        inStockDetailVO.setStatus(inStock.getStatus());
        inStockDetailVO.setTotal(inStockItemVOS.size());
        inStockDetailVO.setItemVOS(inStockItemVOS);
        inStockDetailVO.setSupplierVO(supplierVO);
        return inStockDetailVO;
    }

    @Override
    public void addIntoStock(InStockVO inStockVO) {
        inStockVO.setCreateTime(new Date());
        inStockVO.setModified(new Date());
        InStock inStock = new InStock();
        BeanUtils.copyProperties(inStockVO, inStock);
        if (inStockVO.getSupplierId() == null) {
            SupplierVO supplierVO = new SupplierVO();
            supplierVO.setCreateTime(new Date());
            supplierVO.setModifiedTime(new Date());
            supplierVO.setName(inStockVO.getName());
            supplierVO.setAddress(inStockVO.getAddress());
            supplierVO.setEmail(inStockVO.getEmail());
            supplierVO.setPhone(inStockVO.getPhone());
            supplierVO.setSort(inStockVO.getSort());
            supplierVO.setContact(inStockVO.getContact());
            Supplier supplier = new Supplier();
            BeanUtils.copyProperties(supplierVO, supplier);
            supplyMapper.insert(supplier);
            inStock.setSupplierId(supplier.getId());
        }
        inStock.setStatus(2);
        List<Object> products = inStockVO.getProducts();
        String jsonString = JSON.toJSONString(products);
        List<Map> productsList = JSON.parseArray(jsonString, Map.class);

        AtomicReference<Integer> productNum = new AtomicReference<>(0);
        inStock.setInNum(UUID.randomUUID().toString().replaceAll("-", ""));
        productsList.forEach(map -> {
            Integer productId = (Integer) map.get("productId");
            Integer productNumber = (Integer) map.get("productNumber");
            productNum.updateAndGet(v -> v + productNumber);
            String pNum = productMapper.selectByPrimaryKey(productId).getPNum();
            InStockInfo inStockInfo = new InStockInfo();
            inStockInfo.setInNum(inStock.getInNum());
            inStockInfo.setPNum(pNum);
            inStockInfo.setProductNumber(productNumber);
            inStockInfo.setCreateTime(new Date());
            inStockInfo.setModifiedTime(new Date());
            inStockInfoMapper.insert(inStockInfo);
        });
        inStock.setProductNumber(productNum.get());

        inStockMapper.insert(inStock);
    }

    @Override
    public void publish(Long inStockId) {
        InStock inStock = inStockMapper.selectByPrimaryKey(inStockId);
        inStock.setStatus(0);
        inStockMapper.updateByPrimaryKeySelective(inStock);
        String inNum = inStock.getInNum();
        Example o = new Example(InStockInfo.class);
        Example.Criteria c = o.createCriteria();
        c.andEqualTo("inNum", inNum);
        List<InStockInfo> inStockInfos = inStockInfoMapper.selectByExample(o);
        inStockInfos.forEach(inStockInfo -> {
            String pNum = inStockInfo.getPNum();
            Integer productNumber = inStockInfo.getProductNumber();
            Example example = new Example(ProductStock.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("pNum", pNum);
            ProductStock productStock = productStockMapper.selectOneByExample(example);
            if (productStock != null) {
                Long stock = productStock.getStock();
                stock += productNumber;
                productStockMapper.updateByPNum(pNum, stock);
            } else {
                ProductStock ps = new ProductStock();
                ps.setPNum(pNum);
                ps.setStock(productNumber.longValue());
                productStockMapper.insert(ps);
            }
        });
    }
}
