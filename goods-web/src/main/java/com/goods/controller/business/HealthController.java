package com.goods.controller.business;

import com.goods.business.service.HealthService;
import com.goods.common.response.ActiveUser;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.HealthVO;
import com.goods.common.vo.system.PageVO;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * title:
 *
 * @Author xu
 * @Date 2022/09/25 22:33
 * @FileName: HealthController
 */
@RestController
@RequestMapping("business/health")
public class HealthController {
    @Autowired
    private HealthService healthService;

    // history?pageSize=4&pageNum=1
    @GetMapping("history")
    public ResponseBean history(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize) {
        PageVO<HealthVO> healthVOPageVO = healthService.history(pageNum, pageSize);
        return ResponseBean.success(healthVOPageVO);
    }

    @GetMapping("isReport")
    public Map isReport() {
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        return healthService.isReport(activeUser);
    }

    @PostMapping("report")
    public Map report(@RequestBody HealthVO healthVO) {
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        return healthService.report(healthVO, activeUser);
    }
}
