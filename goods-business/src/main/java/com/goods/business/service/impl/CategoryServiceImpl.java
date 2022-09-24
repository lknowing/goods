package com.goods.business.service.impl;

import com.goods.business.converter.ProductCategoryTreeNodeVOConverter;
import com.goods.business.mapper.ProductCategoryMapper;
import com.goods.business.service.CategoryService;
import com.goods.common.model.business.ProductCategory;
import com.goods.common.utils.CategoryTreeBuilder;
import com.goods.common.utils.ListPageUtils;
import com.goods.common.vo.business.ProductCategoryTreeNodeVO;
import com.goods.common.vo.business.ProductCategoryVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * title:
 *
 * @Author xu
 * @Date 2022/09/23 11:35
 * @FileName: CategoryServiceImpl
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Autowired
    private ProductCategoryTreeNodeVOConverter productCategoryTreeNodeVOConverter;

    @Override
    public PageVO<ProductCategoryTreeNodeVO> categoryTree(Integer pageNum, Integer pageSize) {
        List<ProductCategory> productCategoryList = productCategoryMapper.selectAll();
        List<ProductCategoryTreeNodeVO> productCategoryTreeNodeVOList
                = productCategoryTreeNodeVOConverter.converterToProductCategoryTreeNodeVOList(productCategoryList);
        List<ProductCategoryTreeNodeVO> categoryTreeNodeVOS = CategoryTreeBuilder.build(productCategoryTreeNodeVOList);
        if (pageNum == null && pageSize == null) {
            return new PageVO<>(categoryTreeNodeVOS.size(), categoryTreeNodeVOS);
        }
        List<ProductCategoryTreeNodeVO> page = ListPageUtils.page(categoryTreeNodeVOS, pageSize, pageNum);
        return new PageVO<>(categoryTreeNodeVOS.size(), page);
    }

    @Override
    public List<ProductCategoryTreeNodeVO> getParentCategoryTree() {
        List<ProductCategory> productCategoryList = productCategoryMapper.selectAll();
        List<ProductCategoryTreeNodeVO> productCategoryTreeNodeVOList
                = productCategoryTreeNodeVOConverter.converterToProductCategoryTreeNodeVOList(productCategoryList);
        List<ProductCategoryTreeNodeVO> parentCategoryTree = CategoryTreeBuilder.buildParent(productCategoryTreeNodeVOList);
        return parentCategoryTree;
    }

    @Override
    public void addProductCategory(ProductCategory productCategory) {
        if (productCategory != null) {
            productCategory.setCreateTime(new Date());
            productCategory.setModifiedTime(new Date());
            productCategoryMapper.insert(productCategory);
        }
    }

    @Override
    public ProductCategoryVO findByCategoryId(Long categoryId) {
        ProductCategory productCategory = productCategoryMapper.selectByPrimaryKey(categoryId);
        if (productCategory != null) {
            ProductCategoryVO productCategoryVO = new ProductCategoryVO();
            BeanUtils.copyProperties(productCategory, productCategoryVO);
            return productCategoryVO;
        }
        return null;
    }

    @Override
    public void update(Long categoryId, ProductCategoryVO productCategoryVO) {
        if (productCategoryVO != null) {
            ProductCategory productCategory = new ProductCategory();
            BeanUtils.copyProperties(productCategoryVO, productCategory);
            productCategory.setModifiedTime(new Date());
            productCategoryMapper.updateByPrimaryKey(productCategory);
        }
    }

    @Override
    public void delete(Long categoryId) {
        productCategoryMapper.deleteByPrimaryKey(categoryId);
    }
}
