package com.wxw.controller.seckill;

import com.wxw.common.exception.RrException;
import com.wxw.common.response.Result;
import com.wxw.service.SecKillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

/**
 * @author weixiaowei
 * @desc: 单机简化版秒杀（不要较真 这只是一个模拟多线程秒杀案例）
 * @date: 2021/7/12
 */
@Slf4j
@RestController
@RequestMapping("/secKill")
public class SimpleSecKillController {

    @Resource
    private ThreadPoolTaskExecutor threadPoolExecutor;

    @Resource
    private SecKillService secKillService;

    /**
     * QPS 设置
     */
    private static final int skillNum = 10;

    /**
     * 倒计数锁存器
     */
    private final CountDownLatch latch = new CountDownLatch(skillNum);

    /**
     * 秒杀1：单进程+无锁 = 导致超卖的错误示范
     *
     * @param secKillId
     * @return
     * @url curl -X POST --data "secKillId=1000" http://localhost:8001/secKill/singleProcess/noLock
     */
    @PostMapping("/singleProcess/noLock")
    public Result start(final long secKillId) {
        // 删除秒杀售卖商品记录
        clearSecKillRecord(secKillId);
        log.info("开始秒杀一(会出现超卖)");
        // 十个线程一起抢
        for (int i = 0; i < skillNum; i++) {
            final long userId = i;
            Runnable task = () -> {
                // 坏蛋说 抛异常影响最终效果
                try {
                    Result result = secKillService.startSecKill(secKillId, userId);
                    if (result != null) {
                        log.info("用户:{}{}", userId, result.get("msg"));
                    } else {
                        log.info("用户:{}{}", userId, "哎呦喂，人也太多了，请稍后！");
                    }
                } catch (RrException e) {
                    log.error("哎呀报错了{}", e.getMsg());
                }
                latch.countDown();
            };
            threadPoolExecutor.execute(task);
        }
        try {
            latch.await();// 等待所有人任务结束
            Long seckillCount = secKillService.getSecKillCount(secKillId);
            log.info("一共秒杀出{}件商品", seckillCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.ok("秒杀测试完成");
    }

    /**
     * 秒杀2：单进程+线程锁 解决超买问题（实际上并未解决，因为存在快照重复读取的问题可以使用 共享锁或者排它锁解决）
     *
     * @param secKillId
     * @return
     * @url curl -X POST --data "secKillId=1000" http://localhost:8001/secKill/singleProcess/hasLock
     */
    @PostMapping("/singleProcess/hasLock")
    public Result startByLock(long secKillId) {
        // 删除秒杀售卖商品记录
        clearSecKillRecord(secKillId);
        final long killId = secKillId;
        log.info("开始秒杀一(解决超买问题)");
        // 十个线程 一起抢
        for (int i = 0; i < skillNum; i++) {
            final long userId = i;
            Runnable task = () -> {
                // 坏蛋说 抛异常影响最终效果
                try {
                    Result result = secKillService.startSecKillByThreadLock(killId, userId);
                    if (result != null) {
                        log.info("用户:{}{}", userId, result.get("msg"));
                    } else {
                        log.info("用户:{}{}", userId, "哎呦喂，人也太多了，请稍后！");
                    }
                } catch (RrException e) {
                    log.error("哎呀报错了{}", e.getMsg());
                }
                latch.countDown();
            };
            threadPoolExecutor.execute(task);
        }
        try {
            latch.await();// 等待所有人任务结束
            Long seckillCount = secKillService.getSecKillCount(secKillId);
            log.info("一共秒杀出{}件商品", seckillCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.ok("秒杀测试完成");
    }

    /**
     * 秒杀2.1：单进程+自定义注解线程锁 解决超买问题
     *
     * @param secKillId
     * @return
     * @url curl -X POST --data "secKillId=1000" http://localhost:8001/secKill/singleProcess/hasLockWithAop
     */
    @PostMapping("/singleProcess/hasLockWithAop")
    public Result startByLockWithAop(long secKillId) {
        // 删除秒杀售卖商品记录
        clearSecKillRecord(secKillId);
        final long killId = secKillId;
        log.info("开始秒杀一(解决超买问题)");
        // 十个线程 一起抢
        for (int i = 0; i < skillNum; i++) {
            final long userId = i;
            Runnable task = () -> {
                // 坏蛋说 抛异常影响最终效果
                try {
                    Result result = secKillService.startSecKillByThreadLockWithAop(killId, userId);
                    if (result != null) {
                        log.info("用户:{}{}", userId, result.get("msg"));
                    } else {
                        log.info("用户:{}{}", userId, "哎呦喂，人也太多了，请稍后！");
                    }
                } catch (RrException e) {
                    log.error("哎呀报错了{}", e.getMsg());
                }
                latch.countDown();
            };
            threadPoolExecutor.execute(task);
        }
        try {
            latch.await();// 等待所有人任务结束
            Long seckillCount = secKillService.getSecKillCount(secKillId);
            log.info("一共秒杀出{}件商品", seckillCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.ok("秒杀测试完成");
    }

    /**
     * 秒杀3：单进程+数据库悲观锁 解决超买问题
     * @param secKillId
     * @return 校验库存  使用 for update  悲观锁
     * @url curl -X POST --data "secKillId=1000" http://localhost:8001/secKill/singleProcess/hasLockWithPessimistic
     */
    @PostMapping("/singleProcess/hasLockWithPessimistic")
    public Result startByLockWithPessimistic(long secKillId) {
        // 删除秒杀售卖商品记录
        clearSecKillRecord(secKillId);
        final long killId = secKillId;
        log.info("开始秒杀一(解决超买问题)");
        // 十个线程 一起抢
        for (int i = 0; i < skillNum; i++) {
            final long userId = i;
            Runnable task = () -> {
                // 坏蛋说 抛异常影响最终效果
                try {
                    Result result = secKillService.startSecKillByThreadLockWithPessimistic(killId, userId);
                    if (result != null) {
                        log.info("用户:{}{}", userId, result.get("msg"));
                    } else {
                        log.info("用户:{}{}", userId, "哎呦喂，人也太多了，请稍后！");
                    }
                } catch (RrException e) {
                    log.error("哎呀报错了{}", e.getMsg());
                }
                latch.countDown();
            };
            threadPoolExecutor.execute(task);
        }
        try {
            latch.await();// 等待所有人任务结束
            Long seckillCount = secKillService.getSecKillCount(secKillId);
            log.info("一共秒杀出{}件商品", seckillCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.ok("秒杀测试完成");
    }

    /**
     * 秒杀3.1：单进程+数据库悲观锁 解决超买问题
     * @param secKillId
     * @return 扣减库存和校验库存合并 二次校验 悲观锁 [不推荐update存在锁表的情况]
     * @url curl -X POST --data "secKillId=1000" http://localhost:8001/secKill/singleProcess/hasLockWithPessimistic-1
     */
    @PostMapping("/singleProcess/hasLockWithPessimistic-1")
    public Result startByLockWithPessimistic01(long secKillId) {
        // 删除秒杀售卖商品记录
        clearSecKillRecord(secKillId);
        final long killId = secKillId;
        log.info("开始秒杀一(解决超买问题)");
        // 十个线程 一起抢
        for (int i = 0; i < skillNum; i++) {
            final long userId = i;
            Runnable task = () -> {
                // 坏蛋说 抛异常影响最终效果
                try {
                    Result result = secKillService.startSecKillByThreadLockWithPessimistic01(killId, userId);
                    if (result != null) {
                        log.info("用户:{}{}", userId, result.get("msg"));
                    } else {
                        log.info("用户:{}{}", userId, "哎呦喂，人也太多了，请稍后！");
                    }
                } catch (RrException e) {
                    log.error("哎呀报错了{}", e.getMsg());
                }
                latch.countDown();
            };
            threadPoolExecutor.execute(task);
        }
        try {
            latch.await();// 等待所有人任务结束
            Long seckillCount = secKillService.getSecKillCount(secKillId);
            log.info("一共秒杀出 {} 件商品", seckillCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.ok("秒杀测试完成");
    }

    /**
     * 秒杀4：单进程+数据库锁【乐观锁】
     * @param secKillId
     * @return  根据版本号维护扣减库存  如果配置的抢购人数比较少、比如120:100(人数:商品) 会出现少买的情况
     * @url curl -X POST --data "secKillId=1000" http://localhost:8001/secKill/singleProcess/hasLockWithOptisimistic
     */
    @PostMapping("/singleProcess/hasLockWithOptisimistic")
    public Result startByLockWithOptisimistic(long secKillId) {
        // 删除秒杀售卖商品记录
        clearSecKillRecord(secKillId);
        // 版本重置
        secKillService.updateVersionBySecKillId(secKillId);
        final long killId = secKillId;
        log.info("开始秒杀一(解决超买问题)");
        // 十个线程 一起抢
        for (int i = 0; i < skillNum; i++) {
            final long userId = i;
            Runnable task = () -> {
                // 坏蛋说 抛异常影响最终效果
                try {
                    // 这里使用的乐观锁、可以自定义抢购数量、如果配置的抢购人数比较少、比如120:100(人数:商品) 会出现少买的情况
                    // 用户同时进入会出现更新失败的情况
                    Result result = secKillService.startSecKillByThreadLockWithOptisimistic(killId, userId,1);
                    if (result != null) {
                        log.info("用户:{}{}", userId, result.get("msg"));
                    } else {
                        log.info("用户:{}{}", userId, "哎呦喂，人也太多了，请稍后！");
                    }
                } catch (RrException e) {
                    log.error("哎呀报错了{}", e.getMsg());
                }
                latch.countDown();
            };
            threadPoolExecutor.execute(task);
        }
        try {
            latch.await();// 等待所有人任务结束
            Long seckillCount = secKillService.getSecKillCount(secKillId);
            log.info("一共秒杀出 {} 件商品", seckillCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.ok("秒杀测试完成");
    }


    private void clearSecKillRecord(long secKillId) {
        secKillService.deleteSecKill(secKillId);
    }

}
