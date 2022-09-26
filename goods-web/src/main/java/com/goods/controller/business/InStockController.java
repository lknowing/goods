package com.goods.controller.business;

import com.goods.business.service.InStockService;
import com.goods.common.response.ActiveUser;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.InStockDetailVO;
import com.goods.common.vo.business.InStockVO;
import com.goods.common.vo.system.PageVO;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * title:
 *
 * @Author xu
 * @Date 2022/09/24 11:47
 * @FileName: StockController
 */
@RestController
@RequestMapping("business/inStock")
public class InStockController {
    @Autowired
    private InStockService inStockService;

    @PutMapping("publish/{inStockId}")
    public ResponseBean publish(@PathVariable Long inStockId) {
        try {
            if (inStockId != null) {
                inStockService.publish(inStockId);
                return ResponseBean.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("errorMsg", "异常");
        return ResponseBean.error(map);
    }

    @PostMapping("addIntoStock")
    public ResponseBean addIntoStock(@RequestBody InStockVO inStockVO) {
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        inStockVO.setOperator(activeUser.getUser().getUsername());
        inStockService.addIntoStock(inStockVO);
        return ResponseBean.success();
    }

    @GetMapping("findInStockList")
    public ResponseBean findInStockList(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize,
                                        InStockVO inStockVO) {
        try {
            if (inStockVO != null) {
                PageVO<InStockVO> inStockListPageVo = inStockService.findInStockList(pageNum, pageSize, inStockVO);
                return ResponseBean.success(inStockListPageVo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("errorMsg", "异常");
        return ResponseBean.error(map);
    }

    @PutMapping("remove/{inStockId}")
    public ResponseBean remove(@PathVariable Long inStockId) {
        try {
            if (inStockId != null) {
                inStockService.remove(inStockId);
                return ResponseBean.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("errorMsg", "异常");
        return ResponseBean.error(map);
    }

    @DeleteMapping("delete/{inStockId}")
    public ResponseBean delete(@PathVariable Long inStockId) {
        try {
            if (inStockId != null) {
                inStockService.delete(inStockId);
                return ResponseBean.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("errorMsg", "异常");
        return ResponseBean.error(map);
    }

    @PutMapping("back/{inStockId}")
    public ResponseBean back(@PathVariable Long inStockId) {
        try {
            if (inStockId != null) {
                inStockService.back(inStockId);
                return ResponseBean.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("errorMsg", "异常");
        return ResponseBean.error(map);
    }

    // detail/117?pageNum=1
    @GetMapping("detail/{inStockId}")
    public ResponseBean detail(@PathVariable Long inStockId,
                               @RequestParam Integer pageNum) {
        try {
            InStockDetailVO inStockDetailVO = inStockService.detail(inStockId, pageNum);
            return ResponseBean.success(inStockDetailVO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("errorMsg", "异常");
        return ResponseBean.error(map);
    }
}
