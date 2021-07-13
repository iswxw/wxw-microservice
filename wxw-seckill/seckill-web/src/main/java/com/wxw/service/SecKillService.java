package com.wxw.service;

import com.wxw.common.response.Result;

/**
 * @author weixiaowei
 * @desc: 秒杀业务
 * @date: 2021/7/12
 */
public interface SecKillService {

    /**
     * 秒杀1、会出现数量错误
     * @param killId
     * @param userId
     * @return
     */
    Result startSecKill(long killId, long userId);

    /**
     *  秒杀2、通过线程锁 解决超买问题()
     * @param killId
     * @param userId
     * @return
     */
    Result startSecKillByThreadLock(long killId, long userId);

    /**
     * 秒杀2.1、通过线程锁 解决超买问题
     * @param killId
     * @param userId
     * @return
     */
    Result startSecKillByThreadLockWithAop(long killId, long userId);

    /**
     * 秒杀3、数据库悲观锁
     * @param killId
     * @param userId
     * @return
     */
    Result startSecKillByThreadLockWithPessimistic(long killId, long userId);

    /**
     * 秒杀3.1、数据库悲观锁
     * @param killId
     * @param userId
     * @return 单用户抢购一件商品没有问题、但是抢购多件商品不建议这种写法 UPDATE锁表
     */
    Result startSecKillByThreadLockWithPessimistic01(long killId, long userId);

    /**
     * 数据库乐观锁 单用户抢购一件商品没有问题、但是抢购多件商品不建议这种写法 UPDATE锁表
     * @param killId
     * @param userId
     * @param number 单个用户秒杀商品数量
     * @return
     */
    Result startSecKillByThreadLockWithOptisimistic(long killId, long userId, int number);

    /**
     * 获取秒杀的数量
     * @param secKillId
     * @return
     */
    Long getSecKillCount(long secKillId);

    /**
     * 删除秒杀售卖商品记录
     * @param secKillId
     */
    void deleteSecKill(long secKillId);

    /**
     * 版本重置
     * @param secKillId
     */
    void updateVersionBySecKillId(long secKillId);
}
