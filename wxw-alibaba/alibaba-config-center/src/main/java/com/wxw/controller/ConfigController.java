package com.wxw.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author weixiaowei
 * @desc: 配置自动刷新
 * @date: 2021/7/10
 */
@RestController
@RequestMapping("/config")
@RefreshScope // 支持nacos动态刷新功能
public class ConfigController {

    /**
     * 此配置是通过 nacos 动态配置实现的
     * 测试地址 curl -X POST http://localhost:8000/config/get
     */
    @Value("${useLocalCache:false}")
    private boolean useLocalCache;

    @PostMapping("/get")
    public boolean get() {
        return useLocalCache;
    }

}
