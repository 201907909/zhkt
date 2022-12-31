package com.wisdom.stclass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author WisdomCR
 * @since  2022/10/18
 */
@SpringBootApplication
@EnableRedisHttpSession
@EnableDiscoveryClient
@EnableFeignClients(basePackages={"com.wisdom.**"})
public class ClassApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClassApplication.class, args);
    }
}
