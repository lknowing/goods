package com.goods.controller.business;

import com.goods.business.service.OutStockService;
import com.goods.common.response.ActiveUser;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.OutStockDetailVO;
import com.goods.common.vo.business.OutStockVO;
import com.goods.common.vo.system.PageVO;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * title:
 *
 * @Author xu
 * @Date 2022/09/26 08:22
 * @FileName: OutStockController
 */
@RestController
@RequestMapping("business/outStock")
public class OutStockController {
    @Autowired
    private OutStockService outStockService;

    @PutMapping("publish/{outStockId}")
    public ResponseBean publish(@PathVariable Long outStockId) {
        ResponseBean responseBean = outStockService.publish(outStockId);
        return responseBean;

    }

    @PostMapping("addIntoStock")
    public ResponseBean addIntoStock(@RequestBody OutStockVO outStockVO) {
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        outStockVO.setOperator(activeUser.getUser().getUsername());
        outStockService.addIntoStock(outStockVO);
        return ResponseBean.success();
    }

    // business/outStock/findOutStockList
    @GetMapping("findOutStockList")
    public ResponseBean findOutStockList(@RequestParam Integer pageNum,
                                         @RequestParam Integer pageSize,
                                         OutStockVO outStockVO) {
        try {
            PageVO<OutStockVO> stockVOPageVO = outStockService.findOutStockList(pageNum, pageSize, outStockVO);
            return ResponseBean.success(stockVOPageVO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("errorMsg", "异常");
        return ResponseBean.error(map);
    }

    @PutMapping("remove/{outStockId}")
    public ResponseBean remove(@PathVariable Long outStockId) {
        try {
            if (outStockId != null) {
                outStockService.remove(outStockId);
                return ResponseBean.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("errorMsg", "异常");
        return ResponseBean.error(map);
    }

    @DeleteMapping("delete/{outStockId}")
    public ResponseBean delete(@PathVariable Long outStockId) {
        try {
            if (outStockId != null) {
                outStockService.delete(outStockId);
                return ResponseBean.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("errorMsg", "异常");
        return ResponseBean.error(map);
    }

    @PutMapping("back/{outStockId}")
    public ResponseBean back(@PathVariable Long outStockId) {
        try {
            if (outStockId != null) {
                outStockService.back(outStockId);
                return ResponseBean.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("errorMsg", "异常");
        return ResponseBean.error(map);
    }

    @GetMapping("detail/{outStockId}")
    public ResponseBean detail(@PathVariable Long outStockId,
                               @RequestParam Integer pageNum) {
        try {
            OutStockDetailVO outStockDetailVO = outStockService.detail(outStockId, pageNum);
            return ResponseBean.success(outStockDetailVO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("errorMsg", "异常");
        return ResponseBean.error(map);
    }
}
