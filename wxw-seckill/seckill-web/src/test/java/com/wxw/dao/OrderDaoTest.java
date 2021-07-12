package com.wxw.dao;

import com.wxw.SecKillTest;
import com.wxw.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author weixiaowei
 * @desc:
 * @date: 2021/7/12
 */
@Slf4j
public class OrderDaoTest extends SecKillTest {

    @Resource
    private OrderDao orderDao;

    @Test
    public void test_query_order() {
        Order order = orderDao.selectById(1);
        log.info("orderDao = {}",order);
    }
}
