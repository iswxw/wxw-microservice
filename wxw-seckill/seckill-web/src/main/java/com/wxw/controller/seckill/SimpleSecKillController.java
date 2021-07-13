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
     * 下单接口
     * 秒杀一：单进程+无锁 = 导致超卖的错误示范
     * @url curl -X POST --data "seckillId=1000" http://localhost:8001/secKill/singleProcess/noLock
     * @param seckillId
     * @return
     */
    @PostMapping("/singleProcess/noLock")
    public Result start(long seckillId) {
        int skillNum = 10;
        final CountDownLatch latch = new CountDownLatch(skillNum);
        // 删除秒杀售卖商品记录
        secKillService.deleteSecKill(seckillId);
        final long killId = seckillId;
        log.info("开始秒杀一(会出现超卖)");
        // 十个线程一起抢
        for (int i = 0; i < skillNum; i++) {
            final long userId = i;
            Runnable task = () -> {
                // 坏蛋说 抛异常影响最终效果
                try {
                    Result result = secKillService.startSecKill(killId, userId);
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
            Long seckillCount = secKillService.getSecKillCount(seckillId);
            log.info("一共秒杀出{}件商品", seckillCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.ok("秒杀测试完成");
    }



}
