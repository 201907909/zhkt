package com.wisdom.user.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@Data
@ConfigurationProperties(prefix = "spring.mail")
@Component
public class MailProperties {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private String host;
    private String username;
    // ① 授权码 ② 后者 发件人邮箱登录密码也可
    private String password;
    private String protocol = "smtp";
    // 发件人真实邮箱
    @Value("${spring.mail.username}")
    private String from;
    @Value("${spring.mail.name}")
    private String name;//发送名称
    private Charset defaultEncoding = DEFAULT_CHARSET;
    private Map<String, String> properties = new HashMap<>();
}
