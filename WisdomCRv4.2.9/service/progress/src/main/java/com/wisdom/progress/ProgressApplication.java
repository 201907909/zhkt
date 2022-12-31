package com.wisdom.progress;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author WisdomCR
 * @since  2022/10/18
 */
@SpringBootApplication
@EnableRedisHttpSession
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.wisdom.**")
@EnableFeignClients(basePackages={"com.wisdom.**"})
public class ProgressApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProgressApplication.class, args);
    }
}
