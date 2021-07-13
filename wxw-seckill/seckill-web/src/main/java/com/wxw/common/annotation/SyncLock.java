package com.wxw.common.annotation;

import java.lang.annotation.*;

/**
 * @author weixiaowei
 * @desc: 同步锁 自定义注解
 * @date: 2021/7/13
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SyncLock {
    String description()  default "";
}
