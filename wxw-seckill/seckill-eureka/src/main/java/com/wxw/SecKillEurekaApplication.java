package com.wxw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * URL http://localhost:8000/
 * @author weixiaowei
 * @desc: 秒杀注册中心
 * @date: 2021/7/11
 */
@EnableEurekaServer
@SpringBootApplication
public class SecKillEurekaApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecKillEurekaApplication.class,args);
    }
}
