package com.wisdom.interaction.config;

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
                .apiBasePackage("com.wisdom.interaction.controller")
                .title("智慧课室")
                .description("智慧课室课程互动管理管理接口文档")
                .contactName("")
                .version("1.0")
//                .enableSecurity(true)
                .build();
    }
}
