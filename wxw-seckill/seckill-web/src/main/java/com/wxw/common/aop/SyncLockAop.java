package com.wxw.common.aop;

import com.wxw.common.exception.RrException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author weixiaowei
 * @desc:
 * @order order越小越是最先执行，但更重要的是最先执行的最后结束。order默认值是2147483647
 * @date: 2021/7/11
 */
@Slf4j
@Order(1)
// 切面
@Aspect
@Component
public class SyncLockAop {

    /**
     * 思考：为什么不用synchronized
     * - service 默认是单例的，并发下lock只有一个实例
     * - 互斥锁 参数默认false，不公平锁
     */
    private static Lock lock = new ReentrantLock(true);

    // service层 切入点  用于记录错误日志
    @Pointcut("@annotation(com.wxw.common.annotation.SyncLock)")
    public void lockAspect() {
        log.info("SyncLock 介入增强处理程序");
    }

    // 增强处理
    @Around("lockAspect()")
    public Object around(ProceedingJoinPoint joinPoint) {
        lock.lock();
        Object obj = null;
        try {
            // 原有的程序逻辑
            obj = joinPoint.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw new RrException("SyncLock 同步锁执行异常");
        } finally {
            lock.unlock();
        }
        return obj;
    }

}
