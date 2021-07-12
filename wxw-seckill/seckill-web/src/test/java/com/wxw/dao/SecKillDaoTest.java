package com.wxw.dao;

import com.wxw.SecKillTest;
import com.wxw.domain.SecKill;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @author weixiaowei
 * @desc:
 * @date: 2021/7/12
 */
@Slf4j
public class SecKillDaoTest extends SecKillTest {

    @Resource
    private SecKillDao secKillDao;

    @Test
    public void test_create_order() {
        SecKill secKill = secKillDao.selectById(1000);
        log.info("secKill = {}",secKill);
    }

}
