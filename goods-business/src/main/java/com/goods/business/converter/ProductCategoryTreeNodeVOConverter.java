package com.goods.business.converter;

import com.goods.common.model.business.ProductCategory;
import com.goods.common.model.system.User;
import com.goods.common.vo.business.ProductCategoryTreeNodeVO;
import com.goods.common.vo.system.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * title:
 *
 * @Author xu
 * @Date 2022/09/23 12:33
 * @FileName: ProductCategoryTreeNodeVOConverter
 */
@Component
public class ProductCategoryTreeNodeVOConverter {

    /**
     * 转voList
     *
     * @param productCategoryList
     * @return
     */
    public List<ProductCategoryTreeNodeVO> converterToProductCategoryTreeNodeVOList(List<ProductCategory> productCategoryList) {
        if (!CollectionUtils.isEmpty(productCategoryList)) {
            List<ProductCategoryTreeNodeVO> productCategoryTreeNodeVOS = productCategoryList.stream().map(productCategory -> {
                ProductCategoryTreeNodeVO productCategoryTreeNodeVO = new ProductCategoryTreeNodeVO();
                BeanUtils.copyProperties(productCategory, productCategoryTreeNodeVO);
                return productCategoryTreeNodeVO;
            }).collect(Collectors.toList());
            return productCategoryTreeNodeVOS;
        }
        return null;
    }
}
