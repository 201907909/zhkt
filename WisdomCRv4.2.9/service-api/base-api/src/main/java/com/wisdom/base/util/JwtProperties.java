package com.wisdom.base.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * token的前缀约定
 * @author WisdomCR
 * @since  2022/10/26
 */
@Getter
@Setter
@EnableAutoConfiguration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private Long expiration = 604800L;
    private String secret = "mall-admin-secret";
    private String tokenHead = "Bearer ";
    private String tokenHeader = "Authorization";
}

