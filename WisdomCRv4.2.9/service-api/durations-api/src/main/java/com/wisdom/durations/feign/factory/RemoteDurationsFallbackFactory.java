package com.wisdom.durations.feign.factory;

import com.wisdom.durations.feign.RemoteDurationsService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@Slf4j
@Component
public class RemoteDurationsFallbackFactory implements FallbackFactory<RemoteDurationsService> {
    @Override
    public RemoteDurationsService create(Throwable throwable) {
        return new RemoteDurationsService() {

        };
    }
}
