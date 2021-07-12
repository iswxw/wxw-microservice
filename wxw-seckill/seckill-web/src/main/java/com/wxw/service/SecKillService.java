package com.wxw.service;

import com.wxw.common.response.Result;

/**
 * @author weixiaowei
 * @desc: 秒杀业务
 * @date: 2021/7/12
 */
public interface SecKillService {


    /**
     * 删除秒杀售卖商品记录
     * @param secKillId
     */
    void deleteSecKill(long secKillId);

    /**
     * 秒杀1、会出现数量错误
     * @param killId
     * @param userId
     * @return
     */
    Result startSecKill(long killId, long userId);

    /**
     * 获取秒杀的数量
     * @param secKillId
     * @return
     */
    Long getSecKillCount(long secKillId);
}
