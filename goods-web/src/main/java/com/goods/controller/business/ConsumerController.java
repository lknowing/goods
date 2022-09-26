package com.goods.controller.business;

import com.goods.business.service.ConsumerService;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.ConsumerVO;
import com.goods.common.vo.business.SupplierVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * title:
 *
 * @Author xu
 * @Date 2022/09/25 21:18
 * @FileName: ConsumerController
 */
@RestController
@RequestMapping("business/consumer")
public class ConsumerController {
    @Autowired
    private ConsumerService consumerService;

    @GetMapping("findAll")
    public ResponseBean findAll() {
        List<ConsumerVO> consumerVOList = consumerService.findAll();
        return ResponseBean.success(consumerVOList);
    }

    // business/consumer/findConsumerList?pageNum=1&pageSize=10&name=
    @GetMapping("findConsumerList")
    public ResponseBean findConsumerList(@RequestParam Integer pageNum,
                                         @RequestParam Integer pageSize,
                                         ConsumerVO consumerVO) {
        try {
            PageVO<ConsumerVO> consumerVOPageVO = consumerService.findConsumerList(pageNum, pageSize, consumerVO);
            return ResponseBean.success(consumerVOPageVO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("errorMsg", "异常");
        return ResponseBean.error(map);
    }

    @PostMapping("add")
    public ResponseBean add(@RequestBody ConsumerVO consumerVO) {
        try {
            consumerService.add(consumerVO);
            return ResponseBean.success();
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("errorMsg", "异常");
        return ResponseBean.error(map);
    }

    @GetMapping("edit/{consumerId}")
    public ResponseBean edit(@PathVariable Long consumerId) {
        try {
            ConsumerVO consumerVO = consumerService.edit(consumerId);
            return ResponseBean.success(consumerVO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("errorMsg", "异常");
        return ResponseBean.error(map);
    }

    @PutMapping("update/{consumerId}")
    public ResponseBean update(@PathVariable Long consumerId,
                               @RequestBody ConsumerVO consumerVO) {
        try {
            consumerService.update(consumerId, consumerVO);
            return ResponseBean.success();
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("errorMsg", "异常");
        return ResponseBean.error(map);
    }

    @DeleteMapping("delete/{consumerId}")
    public ResponseBean delete(@PathVariable Long consumerId) {
        try {
            consumerService.delete(consumerId);
            return ResponseBean.success();
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("errorMsg", "异常");
        return ResponseBean.error(map);
    }
}
