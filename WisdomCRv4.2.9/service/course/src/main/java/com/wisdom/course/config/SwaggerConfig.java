package com.wisdom.course.config;

import com.wisdom.swagger.config.BaseSwaggerConfig;
import com.wisdom.swagger.domain.SwaggerProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger2的配置信息
 * @author WisdomCR
 * @since  2022/10/18
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {
    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.wisdom.course.controller")
                .title("智慧课室")
                .description("智慧课室章节、实践文档")
                .contactName("")
                .version("1.0")
//                .enableSecurity(true)
                .build();
    }
}
