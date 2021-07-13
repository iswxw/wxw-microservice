package com.wxw.controller.seckill;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author weixiaowei
 * @desc:
 * @date: 2021/7/13
 */
@Slf4j
@RestController
public class LuaLockController {

    @Resource(name = "set")
    private DefaultRedisScript<Boolean> redisScript;

    @Resource(name = "del")
    private DefaultRedisScript<Boolean> redisScriptDel;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * curl http://localhost:8001/luaSet
     * @return
     */
    @GetMapping("/luaSet")
    public ResponseEntity luaWithSet() {
        // 对应 key value
        List<String> keys = Arrays.asList("testLua", "hello lua");
        Boolean execute = stringRedisTemplate.execute(redisScript, keys, "100");
        log.info("set 脚本执行成功 execute = {}",execute);
        return null;
    }

    @GetMapping("/luaDel")
    public ResponseEntity luaDel() {
        List<String> keys = Arrays.asList("testLua");
        Boolean execute = stringRedisTemplate.execute(redisScriptDel, keys,"hello lua");
        return null;
    }
}
