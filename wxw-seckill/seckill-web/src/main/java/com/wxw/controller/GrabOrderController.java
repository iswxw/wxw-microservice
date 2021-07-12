package com.wxw.controller;

import com.wxw.service.GrabOrderService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author weixiaowei
 * @desc: 抢单业务（秒杀）
 * @date: 2021/7/12
 */
@RequestMapping("/grab")
@RestController
public class GrabOrderController {

    @Resource
    @Qualifier("grabNoLockService") // 无锁Service
    private GrabOrderService grabOrderService;

    /**
     * 司机抢单业务
     * @url curl http://localhost:8001/grab/order/1
     * @param orderId
     * @param driverId
     * @return
     */
    @GetMapping("/order/{orderId}")
    public String grabOrderbyLockMysql(
            @PathVariable("orderId") int orderId,
            @RequestParam(value = "driverId",required = false,defaultValue = "10086") int driverId){
        System.out.println("order:"+orderId+",driverId:"+driverId);
        return "抢单完成！";
    }


}
