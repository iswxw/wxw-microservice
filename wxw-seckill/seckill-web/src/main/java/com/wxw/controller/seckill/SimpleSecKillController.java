//package com.wxw.controller.seckill;
//
//import com.wxw.common.exception.RrException;
//import com.wxw.common.response.Result;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.annotation.Resource;
//import java.util.concurrent.CountDownLatch;
//
///**
// * @author weixiaowei
// * @desc: 单机简化版秒杀（不要较真 这只是一个模拟多线程秒杀案例）
// * @date: 2021/7/12
// */
//@Slf4j
//@RestController
//@RequestMapping("/sec-kill")
//public class SimpleSecKillController {
//
//    @Resource
//    private ThreadPoolTaskExecutor threadPoolExecutor;
//
//    // 秒杀一(最low实现)
//    @PostMapping("/start")
//    public Result start(long seckillId) {
//        int skillNum = 10;
//        final CountDownLatch latch = new CountDownLatch(skillNum);
//        seckillService.deleteSeckill(seckillId);
//        final long killId = seckillId;
//        log.info("开始秒杀一(会出现超卖)");
//        /**
//         * 开启新线程之前，将RequestAttributes对象设置为子线程共享这里仅仅是为了测试，
//         * 否则 IPUtils 中获取不到 request 对象用到限流注解的测试用例，都需要加一下两行代码
//         */
//        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        RequestContextHolder.setRequestAttributes(sra, true);
//        for (int i = 0; i < skillNum; i++) {
//            final long userId = i;
//            Runnable task = () -> {
//                // 坏蛋说 抛异常影响最终效果
//                try {
//                    Result result = seckillService.startSeckil(killId, userId);
//                    if (result != null) {
//                        log.info("用户:{}{}", userId, result.get("msg"));
//                    } else {
//                        log.info("用户:{}{}", userId, "哎呦喂，人也太多了，请稍后！");
//                    }
//                } catch (RrException e) {
//                    log.error("哎呀报错了{}", e.getMsg());
//                }
//                latch.countDown();
//            };
//            threadPoolExecutor.execute(task);
//        }
//        try {
//            latch.await();// 等待所有人任务结束
//            Long seckillCount = seckillService.getSeckillCount(seckillId);
//            log.info("一共秒杀出{}件商品", seckillCount);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return Result.ok();
//    }
//
//
//}
