package com.wxw.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author weixiaowei
 * @desc:
 * @date: 2021/7/10
 */
@RestController
public class ProducerController {

    @Value("${server.port}")
    private String serverPort;

    /**
     * curl http://localhost:8001/nacos/producer/2021
     * @param id
     * @return
     */
    @GetMapping(value = "/nacos/producer/{id}")
    public String getServerPort(@PathVariable("id") Integer id) {
        return "nacos registry, serverPort: " + serverPort + ",id = " + id;
    }
}
