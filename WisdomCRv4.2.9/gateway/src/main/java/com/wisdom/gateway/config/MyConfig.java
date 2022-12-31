package com.wisdom.gateway.config;

import org.springframework.context.annotation.Bean;

/**
 * 扩展类，支持自定义白名单资源路径
 * @author WisdomCR
 * @since  2022/10/26
 */
@SuppressWarnings("SpringConfigurationProxyMethods")
public class MyConfig {
    @Bean
    public IgnoreUrlsConfig ignoreUrlsConfig() {
        return new IgnoreUrlsConfig();
    }
}
