package com.wxw.common;

import com.wxw.SecKillTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author weixiaowei
 * @desc:
 * @date: 2021/7/12
 */
@Slf4j
public class ServerMetaInfo extends SecKillTest {

    @Test
    public void test_cpu() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        log.info("availableProcessors = {}",availableProcessors);
    }
}
