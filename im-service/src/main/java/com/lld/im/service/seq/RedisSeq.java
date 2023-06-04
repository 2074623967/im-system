package com.lld.im.service.seq;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tangcj
 * @date 2023/06/04 19:42
 **/
@Service
public class RedisSeq {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public long doGetSeq(String key) {
        return stringRedisTemplate.opsForValue().increment(key);
    }
}
