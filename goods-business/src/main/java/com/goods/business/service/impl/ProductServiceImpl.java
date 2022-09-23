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

import java.util.List;
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
    public PageVO<ProductVO> findProductList(int pageNum, int pageSize, ProductVO productVO) {
        String name = productVO.getName();
        Integer status = productVO.getStatus();
        Long oneCategoryId = productVO.getOneCategoryId();
        Long twoCategoryId = productVO.getTwoCategoryId();
        Long threeCategoryId = productVO.getThreeCategoryId();
        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();
        if (status != null) {
            criteria.andEqualTo("status", status);
        }
        if (!StringUtils.isEmpty(name)) {
            criteria.andLike("name", "%" + name + "%");
        }
        if (oneCategoryId != null && twoCategoryId != null && threeCategoryId != null) {
            criteria.andEqualTo("one_category_id", oneCategoryId);
            criteria.andEqualTo("two_category_id", twoCategoryId);
            criteria.andEqualTo("three_category_id", threeCategoryId);
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
}
