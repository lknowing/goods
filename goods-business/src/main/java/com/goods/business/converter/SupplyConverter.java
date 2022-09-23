package com.goods.business.converter;

import com.goods.common.model.business.ProductCategory;
import com.goods.common.model.business.Supplier;
import com.goods.common.vo.business.ProductCategoryTreeNodeVO;
import com.goods.common.vo.business.SupplierVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * title:
 *
 * @Author xu
 * @Date 2022/09/23 16:23
 * @FileName: supplyConverter
 */
@Component
public class SupplyConverter {
    /**
     * 转voList
     *
     * @param supplierList
     * @return
     */
    public List<SupplierVO> converterToSupplierVOList(List<Supplier> supplierList) {
        if (!CollectionUtils.isEmpty(supplierList)) {
            List<SupplierVO> supplierVOS = supplierList.stream().map(supplier -> {
                SupplierVO supplierVO = new SupplierVO();
                BeanUtils.copyProperties(supplier, supplierVO);
                return supplierVO;
            }).collect(Collectors.toList());
            return supplierVOS;
        }
        return null;
    }
}
