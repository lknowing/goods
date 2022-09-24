package com.goods.controller.business;

import com.goods.business.service.ProductService;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.ProductVO;
import com.goods.common.vo.system.PageVO;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

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
    public ResponseBean findProductList(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize,
                                        ProductVO productVO,
                                        HttpServletRequest request) {
        String categorys = request.getParameter("categorys");
        if (!StringUtils.isEmpty(categorys)) {
            String[] split = categorys.split(",");
            productVO.setOneCategoryId(Long.parseLong(split[0]));
            productVO.setTwoCategoryId(Long.parseLong(split[1]));
            productVO.setThreeCategoryId(Long.parseLong(split[2]));
            Long[] splitLong = (Long[]) ConvertUtils.convert(split, Long.class);
            productVO.setCategoryKeys(splitLong);
        }
        PageVO<ProductVO> productVOPageVO = productService.findProductList(pageNum, pageSize, productVO);
        return ResponseBean.success(productVOPageVO);
    }

    @PostMapping("add")
    public ResponseBean add(@RequestBody ProductVO productVO) {
        if (productVO != null) {
            productService.addProduct(productVO);
            return ResponseBean.success();
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("errorMsg", "参数为空");
        return ResponseBean.error(map);
    }

    @PutMapping("publish/{productId}")
    public ResponseBean publish(@PathVariable Long productId) {
        if (productId != null) {
            productService.publish(productId);
            return ResponseBean.success();
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("errorMsg", "异常");
        return ResponseBean.error(map);
    }

    @PutMapping("remove/{productId}")
    public ResponseBean remove(@PathVariable Long productId) {
        if (productId != null) {
            productService.remove(productId);
            return ResponseBean.success();
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("errorMsg", "异常");
        return ResponseBean.error(map);
    }

    @DeleteMapping("delete/{productId}")
    public ResponseBean delete(@PathVariable Long productId) {
        if (productId != null) {
            productService.delete(productId);
            return ResponseBean.success();
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("errorMsg", "异常");
        return ResponseBean.error(map);
    }

    @PutMapping("back/{productId}")
    public ResponseBean back(@PathVariable Long productId) {
        if (productId != null) {
            productService.back(productId);
            return ResponseBean.success();
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("errorMsg", "异常");
        return ResponseBean.error(map);
    }

    @GetMapping("edit/{productId}")
    public ResponseBean edit(@PathVariable Long productId) {
        if (productId != null) {
            ProductVO productVO = productService.edit(productId);
            return ResponseBean.success(productVO);
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("errorMsg", "异常");
        return ResponseBean.error(map);
    }

    @PutMapping("update/{productId}")
    public ResponseBean edit(@PathVariable Long productId,
                             @RequestBody ProductVO productVO) {
        if (productId != null && productVO != null) {
            productService.update(productId, productVO);
            return ResponseBean.success();
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("errorMsg", "异常");
        return ResponseBean.error(map);
    }

}
