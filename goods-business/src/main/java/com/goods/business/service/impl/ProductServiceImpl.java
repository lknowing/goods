package com.goods.business.service.impl;

import com.goods.business.mapper.ProductMapper;
import com.goods.business.service.ProductService;
import com.goods.common.model.business.Product;
import com.goods.common.utils.ListPageUtils;
import com.goods.common.vo.business.ProductVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * title:
 *
 * @Author xu
 * @Date 2022/09/23 20:59
 * @FileName: ProductServiceImpl
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Override
    public PageVO<ProductVO> findProductList(Integer pageNum, Integer pageSize, ProductVO productVO) {
        String name = productVO.getName();
        Integer status = productVO.getStatus();
        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();
        if (status != null) {
            criteria.andEqualTo("status", status);
        }
        if (!StringUtils.isEmpty(name)) {
            criteria.andLike("name", "%" + name + "%");
        }
        if (productVO.getCategoryKeys() != null) {
            Long oneCategoryId = productVO.getOneCategoryId();
            Long twoCategoryId = productVO.getTwoCategoryId();
            Long threeCategoryId = productVO.getThreeCategoryId();
            if (oneCategoryId != null && twoCategoryId != null && threeCategoryId != null) {
                criteria.andEqualTo("oneCategoryId", oneCategoryId);
                criteria.andEqualTo("twoCategoryId", twoCategoryId);
                criteria.andEqualTo("threeCategoryId", threeCategoryId);
            }
        }
        List<Product> productList = productMapper.selectByExample(example);
        List<ProductVO> productVoList = productList.stream().map(product -> {
            ProductVO productVo = new ProductVO();
            BeanUtils.copyProperties(product, productVo);
            return productVo;
        }).collect(Collectors.toList());
        List<ProductVO> page = ListPageUtils.page(productVoList, pageSize, pageNum);
        return new PageVO<>(productVoList.size(), page);
    }

    @Override
    public void addProduct(ProductVO productVO) {
        //        if (productVO.getId() != null) {
        //            // 修改
        //        }
        // 新增
        // 商品编号
        productVO.setPNum(UUID.randomUUID().toString());
        // 时间
        productVO.setCreateTime(new Date());
        productVO.setModifiedTime(new Date());
        // 设置默认状态 2:添加待审核
        productVO.setStatus(2);
        // 读取分类数组
        Long[] categoryKeys = productVO.getCategoryKeys();
        productVO.setOneCategoryId(categoryKeys[0]);
        productVO.setTwoCategoryId(categoryKeys[1]);
        productVO.setThreeCategoryId(categoryKeys[2]);
        // 赋值到实体类
        Product product = new Product();
        BeanUtils.copyProperties(productVO, product);
        productMapper.insert(product);
    }

    @Override
    public void publish(Long productId) {
        Product product = new Product();
        product.setId(productId);
        product.setStatus(0);
        productMapper.updateByPrimaryKeySelective(product);
    }

    @Override
    public void remove(Long productId) {
        Product product = new Product();
        product.setId(productId);
        product.setStatus(1);
        productMapper.updateByPrimaryKeySelective(product);
    }

    @Override
    public void delete(Long productId) {
        productMapper.deleteByPrimaryKey(productId);
    }

    @Override
    public void back(Long productId) {
        Product product = new Product();
        product.setId(productId);
        product.setStatus(0);
        productMapper.updateByPrimaryKeySelective(product);
    }

    @Override
    public ProductVO edit(Long productId) {
        Product product = productMapper.selectByPrimaryKey(productId);
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(product, productVO);
        return productVO;
    }

    @Override
    public void update(Long productId, ProductVO productVO) {
        productVO.setModifiedTime(new Date());
        Product product = new Product();
        BeanUtils.copyProperties(productVO, product);
        productMapper.updateByPrimaryKeySelective(product);
    }
}
