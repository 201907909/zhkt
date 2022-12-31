package com.wisdom.base.util;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import java.util.Date;

/**
 * @author WisdomCR
 * @since 2022-05-10
 */
public class BaseMyBatisPlusConfig implements MetaObjectHandler {
    // 注册乐观锁插件
    @Bean
    public MybatisPlusInterceptor optimisticLockerInnerInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }

    //插入时的填充策略
    @Override
    public void insertFill(MetaObject metaObject) {
        Date date = new Date();
        setFieldValByName("createTime", date, metaObject);
        setFieldValByName("updateTime", date, metaObject);
    }

    //更新时的填充策略
    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName("updateTime", new Date(), metaObject);
    }
}
