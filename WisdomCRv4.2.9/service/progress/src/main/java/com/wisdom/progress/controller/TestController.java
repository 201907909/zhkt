package com.wisdom.progress.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.banner.lib.lock.RedisLock;

import java.util.concurrent.TimeUnit;

/**
 * @author WisdomCR
 * @since  2022/10/18
 */
@RestController
@RequestMapping("/teacherPractice")
public class TestController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public int b = 0;

    @GetMapping("/test")
    public void test() {
        System.out.println(b);
    }


    @GetMapping("/a")
    @RedisLock(name = "a")
    public void a() {

        try {

            Thread.sleep(1000*10);   // 休眠3秒

        } catch (Exception e) {

            System.out.println("Got an exception!");

        }
        b += 1;
    }

}
