package com.goods.business.service.impl;

import com.goods.business.mapper.HealthMapper;
import com.goods.business.service.HealthService;
import com.goods.common.model.business.Health;
import com.goods.common.response.ActiveUser;
import com.goods.common.utils.ListPageUtils;
import com.goods.common.vo.business.HealthVO;
import com.goods.common.vo.system.PageVO;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * title:
 *
 * @Author xu
 * @Date 2022/09/25 22:41
 * @FileName: HealthServiceImpl
 */
@Service
public class HealthServiceImpl implements HealthService {
    @Autowired
    private HealthMapper healthMapper;

    @Override
    public Map isReport(ActiveUser activeUser) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            Long userId = activeUser.getUser().getId();
            Example example = new Example(Health.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("userId", userId);
            Date todayTime = getTodayTime();
            criteria.andGreaterThanOrEqualTo("createTime", todayTime);
            Health health = healthMapper.selectOneByExample(example);
            if (health != null) {
                HealthVO healthVO = new HealthVO();
                BeanUtils.copyProperties(health, healthVO);
                map.put("success", true);
                map.put("data", healthVO);
                return map;
            }
            map.put("success", true);
            map.put("data", false);
            return map;
        } catch (BeansException e) {
            e.printStackTrace();
        }
        map.put("success", false);
        map.put("msg", "异常");
        return map;
    }

    private Date getTodayTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    @SneakyThrows
    @Override
    public Map report(HealthVO healthVO, ActiveUser activeUser) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            Long userId = activeUser.getUser().getId();
            healthVO.setUserId(userId);
            Health health = new Health();
            BeanUtils.copyProperties(healthVO, health);

            health.setCreateTime(new Date());

            healthMapper.insert(health);
            map.put("success", true);
            return map;
        } catch (BeansException e) {
            e.printStackTrace();
        }
        map.put("success", false);
        map.put("msg", "异常");
        return map;
    }

    @Override
    public PageVO<HealthVO> history(Integer pageNum, Integer pageSize) {
        List<Health> healthList = healthMapper.selectAll();
        List<HealthVO> healthVOList = healthList.stream().map(health -> {
            HealthVO healthVO = new HealthVO();
            BeanUtils.copyProperties(health, healthVO);
            return healthVO;
        }).collect(Collectors.toList());
        List<HealthVO> page = ListPageUtils.page(healthVOList, pageSize, pageNum);
        return new PageVO<>(healthVOList.size(), page);
    }

}
