package com.wxw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * @author weixiaowei
 * @desc:
 * @date: 2021/7/10
 */
@RestController
public class ConsumerController {

    private final RestTemplate restTemplate;

    @Value("${service-url.nacos-user-service:http://alibaba-producer-client}")
    private String serverURL;

    /**
     * 发起 RPC
     * curl http://localhost:9001/nacos/consumer/2021
     * @param id
     * @return
     */
    @GetMapping(value = "/nacos/consumer/{id}")
    public String getRpcProducer(@PathVariable("id") Long id) {
        URI uri = URI.create(serverURL + "/nacos/producer/" + id);
        // 远程调用 提供者应用（alibaba-producer-client）的接口 curl http://{service-name}/nacos/producer/2021
        return restTemplate.getForObject(uri, String.class);
    }

    @Autowired
    public ConsumerController(RestTemplate restTemplate) {this.restTemplate = restTemplate;}
}
