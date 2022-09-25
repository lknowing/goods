package com.goods.controller.business;

import com.goods.business.service.SupplyService;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.SupplierVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * title:
 *
 * @Author xu
 * @Date 2022/09/23 15:58
 * @FileName: SupplyController
 */
@RestController
@RequestMapping("business/supplier")
public class SupplyController {
    @Autowired
    private SupplyService supplyService;

    @GetMapping("findAll")
    public ResponseBean findAll() {
       List<SupplierVO> supplierVOList = supplyService.findAll();
        return ResponseBean.success(supplierVOList);
    }

    // business/supplier/findSupplierList?pageNum=1&pageSize=10&name=
    @GetMapping("findSupplierList")
    public ResponseBean<PageVO<SupplierVO>> findSupplierList(@RequestParam("pageNum") int pageNum,
                                                             @RequestParam("pageSize") int pageSize,
                                                             SupplierVO supplierVO) {
        PageVO<SupplierVO> supplierList = supplyService.findSupplierList(pageNum, pageSize, supplierVO);
        return ResponseBean.success(supplierList);
    }

    @PostMapping("add")
    public ResponseBean add(@RequestBody SupplierVO supplierVO) {
        supplyService.add(supplierVO);
        return ResponseBean.success();
    }

    // edit/25
    @GetMapping("edit/{supplierId}")
    public ResponseBean edit(@PathVariable Long supplierId) {
        SupplierVO supplierVO = supplyService.findById(supplierId);
        return ResponseBean.success(supplierVO);
    }

    @PutMapping("update/{supplierId}")
    public ResponseBean update(@PathVariable Long supplierId,
                               @RequestBody SupplierVO supplierVO) {
        supplyService.update(supplierId, supplierVO);
        return ResponseBean.success();
    }

    @DeleteMapping("delete/{supplierId}")
    public ResponseBean delete(@PathVariable Long supplierId) {
        supplyService.delete(supplierId);
        return ResponseBean.success();
    }

}
