package com.goods.business.service;

import com.goods.common.response.ActiveUser;
import com.goods.common.vo.business.HealthVO;
import com.goods.common.vo.system.PageVO;

import java.util.Map;

/**
 * title:
 *
 * @Author xu
 * @Date 2022/09/25 22:41
 * @FileName: HealthService
 */
public interface HealthService {
    Map isReport(ActiveUser activeUser);

    Map report(HealthVO healthVO,ActiveUser activeUser);

    PageVO<HealthVO> history(Integer pageNum, Integer pageSize);
}
