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
    PageVO<ProductVO> findProductList(int pageNum, int pageSize, ProductVO productVO);
}
