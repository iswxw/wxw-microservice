package com.wxw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wxw.common.enums.SeckillStatEnum;
import com.wxw.common.response.Result;
import com.wxw.dao.SecKillDao;
import com.wxw.dao.SuccessKilledDao;
import com.wxw.domain.SuccessKilled;
import com.wxw.service.SecKillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;

/**
 * @author weixiaowei
 * @desc:
 * @date: 2021/7/12
 */
@Slf4j
@Service
public class SecKillServiceImpl implements SecKillService {

    @Resource
    private SecKillDao secKillDao;

    @Resource
    private SuccessKilledDao successKilledDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteSecKill(long secKillId) {
        successKilledDao.deleteByKillId(secKillId);
        secKillDao.updateNumberBySecKillId(secKillId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result startSecKill(long killId, long userId) {
        //1. 校验库存
        Long number = secKillDao.queryNumberBySeckill(killId);
        if (number !=null && number > 0){
            //2.扣库存
            secKillDao.updateNumberByKillId(killId);
            //3.创建订单
            SuccessKilled killed = new SuccessKilled();
            killed.setSeckillId(killId);
            killed.setUserId(userId);
            killed.setState((short) 0);
            killed.setCreateTime(new Timestamp(System.currentTimeMillis()));
            int insert = successKilledDao.insert(killed);
            log.info("insert = {}",insert);
            //4.支付
            return Result.ok(SeckillStatEnum.SUCCESS);
        }else {
            return Result.error(SeckillStatEnum.END);
        }
    }

    @Override
    public Long getSecKillCount(long secKillId) {
        QueryWrapper<SuccessKilled> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("seckill_id",secKillId);
        Integer selectCount = successKilledDao.selectCount(queryWrapper);
        log.info("selectCount = {}",selectCount);
        return selectCount.longValue();
    }
}
