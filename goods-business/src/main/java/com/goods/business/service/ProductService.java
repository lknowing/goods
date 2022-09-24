package com.goods.business.service;

import com.goods.common.vo.business.ProductVO;
import com.goods.common.vo.system.PageVO;

/**
 * title:
 *
 * @Author xu
 * @Date 2022/09/23 20:59
 * @FileName: ProductService
 */
public interface ProductService {
    PageVO<ProductVO> findProductList(Integer pageNum, Integer pageSize, ProductVO productVO);

    void addProduct(ProductVO productVO);

    void publish(Long productId);

    void remove(Long productId);

    void delete(Long productId);

    void back(Long productId);

    ProductVO edit(Long productId);

    void update(Long productId, ProductVO productVO);
}
