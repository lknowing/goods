package com.goods.business.service;

import com.github.pagehelper.Page;
import com.goods.common.model.business.Supplier;
import com.goods.common.vo.business.SupplierVO;
import com.goods.common.vo.system.PageVO;

import java.util.List;

/**
 * title:
 *
 * @Author xu
 * @Date 2022/09/23 15:59
 * @FileName: SupplyServivce
 */
public interface SupplyService {
    PageVO<SupplierVO> findSupplierList(int pageNum, int pageSize, SupplierVO supplierVO);

    void add(SupplierVO supplierVO);

    SupplierVO findById(Long supplierId);

    void update(Long supplierId, SupplierVO supplierVO);

    void delete(Long supplierId);

    List<SupplierVO> findAll();
}
