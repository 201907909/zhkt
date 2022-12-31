package com.wisdom.redis.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 * @author WisdomCR
 * @since 2022-10-17
 */
@Component
public class RedisUtils {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    private final String DEFAULT_KEY_PREFIX = "";
    private final int EXPIRE_TIME = 1;
    private final TimeUnit EXPIRE_TIME_TYPE = TimeUnit.DAYS;

    public Boolean set(String key,String str){
        try {
            if (str != null) {
                stringRedisTemplate.opsForValue().set(DEFAULT_KEY_PREFIX+key, str);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String get(String str){
        String value;
        try {
            value = stringRedisTemplate.opsForValue().get(DEFAULT_KEY_PREFIX + str);
        } catch (Exception e) {
           return null;
        }
        return value;
    }

}
