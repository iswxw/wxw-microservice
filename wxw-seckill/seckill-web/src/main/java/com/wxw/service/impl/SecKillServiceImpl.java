package com.wxw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wxw.common.annotation.SyncLock;
import com.wxw.common.enums.SeckillStatEnum;
import com.wxw.common.exception.RrException;
import com.wxw.common.response.Result;
import com.wxw.dao.SecKillDao;
import com.wxw.dao.SuccessKilledDao;
import com.wxw.domain.SecKill;
import com.wxw.domain.SuccessKilled;
import com.wxw.service.SecKillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

    /**
     * 思考：为什么不用synchronized
     * service 默认是单例的，并发下lock只有一个实例
     * 互斥锁 参数默认false，不公平锁
     */
    private static final Lock lock = new ReentrantLock(true);

    // 秒杀 无锁
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result startSecKill(long killId, long userId) {
        //1. 校验库存
        Long number = secKillDao.queryNumberBySeckill(killId);
        if (number != null && number > 0) {
            //2.扣库存
            secKillDao.updateNumberByKillId(killId);
            //3.创建订单
            SuccessKilled killed = new SuccessKilled();
            killed.setSeckillId(killId);
            killed.setUserId(userId);
            killed.setState((short) 0);
            killed.setCreateTime(new Timestamp(System.currentTimeMillis()));
            int insert = successKilledDao.insert(killed);
            log.info("insert = {}", insert);
            //4.支付
            return Result.ok(SeckillStatEnum.SUCCESS);
        } else {
            return Result.error(SeckillStatEnum.END);
        }
    }

    // 秒杀 线程锁
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result startSecKillByThreadLock(long killId, long userId) {
        lock.lock();
        try {
            /**
             * 这里有个问题: 由于两个事务线程都读了同一份快照导致超买的问题
             * - 简单的select是快照读，没有加锁，A事务还没提交，B事务也直接select快照读了，于是结果集跟A事务一样。
             *   所以这里应该是A事务没提交，B事务就不能开始，也就是A事务的select 要加锁，使用当前读的（共享锁或排它锁）都可以。
             *   这样A事务提交，表锁才释放，B事务才能开启，就不会有这个超卖问题了。
             * 使用如下sql：
             *   悲观锁：SELECT number FROM `seckill-test`.t_sec_kill WHERE seckill_id=#{killId} for update
             *   乐观锁：SELECT number FROM `seckill-test`.t_sec_kill WHERE seckill_id=#{killId} lock in share mode
             * 使用分布式锁（zk、redis），AOP注解形式的线程锁为什么不会出现这个问题
             *  (事实是有问题的，由于redis释放锁需要远程通信，不那么明显而已)
             */
            //1. 校验库存
            Long number = secKillDao.queryNumberBySeckill(killId);
            if (number > 0) {
                log.info(" number测试库存数据大小 = {}", number);
                //2.扣库存
                secKillDao.updateNumberByKillId(killId);
                //3.创建订单
                SuccessKilled killed = new SuccessKilled();
                killed.setSeckillId(killId);
                killed.setUserId(userId);
                killed.setState((short) 0);
                killed.setCreateTime(new Timestamp(System.currentTimeMillis()));
                int insert = successKilledDao.insert(killed);
                log.info("insert = {}", insert);
                // 4.支付流程 ...
            } else {
                // 秒杀结束
                return Result.error(SeckillStatEnum.END);
            }
        } finally {
            lock.unlock();
        }
        return Result.ok(SeckillStatEnum.SUCCESS);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    @SyncLock
    public Result startSecKillByThreadLockWithAop(long killId, long userId) {
        //1. 校验库存
        Long number = secKillDao.queryNumberBySeckill(killId);
        if (number > 0) {
            log.info(" number测试库存数据大小 = {}", number);
            //2.扣库存
            secKillDao.updateNumberByKillId(killId);
            //3.创建订单
            SuccessKilled killed = new SuccessKilled();
            killed.setSeckillId(killId);
            killed.setUserId(userId);
            killed.setState((short) 0);
            killed.setCreateTime(new Timestamp(System.currentTimeMillis()));
            int insert = successKilledDao.insert(killed);
            log.info("insert = {}", insert);
            // 4.支付流程 ...
        } else {
            // 秒杀结束
            return Result.error(SeckillStatEnum.END);
        }
        return Result.ok(SeckillStatEnum.SUCCESS);
    }

    /**
     * 注意这里 限流注解 可能会出现少买 自行调整
     *
     * @param killId
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result startSecKillByThreadLockWithPessimistic(long killId, long userId) {
        //1. 校验库存
        Long number = secKillDao.queryNumberBySeckillAndPessimistic(killId);
        if (number > 0) {
            log.info(" number测试库存数据大小 = {}", number);
            //2.扣库存
            secKillDao.updateNumberByKillId(killId);
            //3.创建订单
            SuccessKilled killed = new SuccessKilled();
            killed.setSeckillId(killId);
            killed.setUserId(userId);
            killed.setState((short) 0);
            killed.setCreateTime(new Timestamp(System.currentTimeMillis()));
            int insert = successKilledDao.insert(killed);
            log.info("insert = {}", insert);
            // 4.支付流程 ...
        } else {
            // 秒杀结束
            return Result.error(SeckillStatEnum.END);
        }
        return Result.ok(SeckillStatEnum.SUCCESS);
    }

    /**
     * 单用户抢购一件商品没有问题、但是抢购多件商品不建议这种写法 UPDATE锁表
     *
     * SHOW STATUS LIKE 'innodb_row_lock%'; 查看行锁使用情况
     * 如果发现锁争用比较严重，如InnoDB_row_lock_waits和InnoDB_row_lock_time_avg的值比较高
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result startSecKillByThreadLockWithPessimistic01(long killId, long userId) {
        //1. 校验库存 和扣减库存合二为一
        //2. 扣库存
        // 单用户抢购一件商品没有问题、但是抢购多件商品不建议这种写法 UPDATE锁表
        int count = secKillDao.updateNumberByKillIdAndNumber(killId);
        //3.创建订单
        if (count > 0) {
            SuccessKilled killed = new SuccessKilled();
            killed.setSeckillId(killId);
            killed.setUserId(userId);
            killed.setState((short) 0);
            killed.setCreateTime(new Timestamp(System.currentTimeMillis()));
            int insert = successKilledDao.insert(killed);
            log.info("insert = {}", insert);
            // 4.支付流程 ...
            return Result.ok(SeckillStatEnum.SUCCESS);
        }
        return Result.error(SeckillStatEnum.END);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result startSecKillByThreadLockWithOptisimistic(long killId, long userId, int number) {
        // 查询需要秒杀的商品记录
        SecKill secKill = secKillDao.selectById(killId);
        // 剩余的数量应该要大于等于秒杀的数量
        if (secKill.getNumber() >= number){
           int count =  secKillDao.updateStockNumberByVersion(killId,number,secKill.getVersion());
           if (count > 0){
               SuccessKilled killed = new SuccessKilled();
               killed.setSeckillId(killId);
               killed.setUserId(userId);
               killed.setState((short) 0);
               killed.setCreateTime(new Timestamp(System.currentTimeMillis()));
               int insert = successKilledDao.insert(killed);
               log.info("insert = {}", insert);
               // 4.支付流程 ...
               return Result.ok(SeckillStatEnum.SUCCESS);
           }
        }
        return Result.error(SeckillStatEnum.END);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteSecKill(long secKillId) {
        successKilledDao.deleteByKillId(secKillId);
        secKillDao.updateNumberBySecKillId(secKillId);
    }

    @Override
    public void updateVersionBySecKillId(long secKillId) {
        secKillDao.updateVersionBySecKillId(secKillId);
    }

    @Override
    public Long getSecKillCount(long secKillId) {
        QueryWrapper<SuccessKilled> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("seckill_id", secKillId);
        Integer selectCount = successKilledDao.selectCount(queryWrapper);
        log.info("selectCount = {}", selectCount);
        return selectCount.longValue();
    }
}
