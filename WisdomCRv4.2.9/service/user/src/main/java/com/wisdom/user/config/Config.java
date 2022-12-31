package com.wisdom.user.config;

import com.wisdom.base.util.JwtProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@Configuration
public class Config {
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public JwtProperties jwtProperties() {
        return new JwtProperties();
    }
}
