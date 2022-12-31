package com.wisdom.type.feign.factory;

import com.wisdom.type.feign.RemoteTypeService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@Slf4j
@Component
public class RemoteTypeFallbackFactory implements FallbackFactory<RemoteTypeService> {
    @Override
    public RemoteTypeService create(Throwable throwable) {
        return new RemoteTypeService() {

        };
    }
}
