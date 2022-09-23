package com.goods.controller.business;

import com.goods.business.service.ProductService;
import com.goods.common.model.business.Product;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.ProductVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * title:
 *
 * @Author xu
 * @Date 2022/09/23 20:12
 * @FileName: ProductController
 */
@RestController
@RequestMapping("business/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    // business/product/findProductList?pageNum=1&pageSize=6&name=&categoryId=&supplier=&status=0
    @GetMapping("findProductList")
    public ResponseBean findProductList(@RequestParam int pageNum,
                                        @RequestParam int pageSize,
                                        ProductVO productVO) {
//        String[] split = categorys.split(",");
//        productVO.setOneCategoryId(Long.parseLong(split[0]));
//        productVO.setTwoCategoryId(Long.parseLong(split[1]));
//        productVO.setThreeCategoryId(Long.parseLong(split[2]));
        PageVO<ProductVO> productVOPageVO = productService.findProductList(pageNum, pageSize, productVO);
        return ResponseBean.success(productVOPageVO);
    }
}
