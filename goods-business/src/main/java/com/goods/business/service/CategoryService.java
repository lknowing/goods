package com.goods.business.service;

import com.goods.common.model.business.ProductCategory;
import com.goods.common.vo.business.ProductCategoryTreeNodeVO;
import com.goods.common.vo.business.ProductCategoryVO;
import com.goods.common.vo.system.PageVO;

import java.util.List;

/**
 * title:
 *
 * @Author xu
 * @Date 2022/09/23 11:34
 * @FileName: CategoryService
 */
public interface CategoryService {
    PageVO<ProductCategoryTreeNodeVO> categoryTree(Integer pageNum, Integer pageSize);

    List<ProductCategoryTreeNodeVO> getParentCategoryTree();

    void addProductCategory(ProductCategory productCategory);

    ProductCategoryVO findByCategoryId(Long categoryId);

    void update(Long categoryId, ProductCategoryVO productCategoryVO);

    void delete(Long categoryId);
}
