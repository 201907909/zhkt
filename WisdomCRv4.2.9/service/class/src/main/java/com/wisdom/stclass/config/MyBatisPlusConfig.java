package com.wisdom.stclass.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.wisdom.base.util.BaseMyBatisPlusConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author WisdomCR
 * @since 2022-10-30
 */
@Configuration
@MapperScan("com.wisdom.stclass.mapper")   // 扫描mapper文件夹
@EnableTransactionManagement    //自动管理事务
public class MyBatisPlusConfig extends BaseMyBatisPlusConfig implements MetaObjectHandler {}
