package com.wxw.common;

import com.wxw.SecKillTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author weixiaowei
 * @desc:
 * @date: 2021/7/13
 */
@Slf4j
public class DataTest extends SecKillTest {

    @Test
    public void test_1() {
        Long a1 = 1L;
        if (a1>0){
            System.out.println("a1 = " + a1);
        }
    }
}
