package com.goods.controller.business;

import com.goods.business.service.CategoryService;
import com.goods.common.model.business.ProductCategory;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.ProductCategoryTreeNodeVO;
import com.goods.common.vo.business.ProductCategoryVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * title:
 *
 * @Author xu
 * @Date 2022/09/23 11:03
 * @FileName: businessController
 */
@RestController
@RequestMapping("business/productCategory")
public class ProductCategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("categoryTree")
    public ResponseBean<PageVO<ProductCategoryTreeNodeVO>> categoryTree(
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        PageVO<ProductCategoryTreeNodeVO> productCategoryTreeNodeVOList = this.categoryService.categoryTree(pageNum, pageSize);
        return ResponseBean.success(productCategoryTreeNodeVOList);
    }

    @GetMapping("getParentCategoryTree")
    public ResponseBean<List<ProductCategoryTreeNodeVO>> getParentCategoryTree() {
        List<ProductCategoryTreeNodeVO> categoryTreeNodeVOList = this.categoryService.getParentCategoryTree();
        return ResponseBean.success(categoryTreeNodeVOList);
    }

    @PostMapping("add")
    public ResponseBean add(@RequestBody ProductCategory productCategory) {
        this.categoryService.addProductCategory(productCategory);
        return ResponseBean.success();
    }

    @GetMapping("edit/{categoryId}")
    public ResponseBean findByCategoryId(@PathVariable Long categoryId) {
        ProductCategoryVO productCategoryVO = this.categoryService.findByCategoryId(categoryId);
        return ResponseBean.success(productCategoryVO);
    }

    // http://www.localhost:8989/business/productCategory/update/85
    @PutMapping("update/{categoryId}")
    public ResponseBean update(@PathVariable Long categoryId,
                               @RequestBody ProductCategoryVO productCategoryVO) {
        this.categoryService.update(categoryId, productCategoryVO);
        return ResponseBean.success();
    }

    @DeleteMapping("delete/{categoryId}")
    public ResponseBean delete(@PathVariable Long categoryId) {
        return this.categoryService.delete(categoryId);
    }

}
