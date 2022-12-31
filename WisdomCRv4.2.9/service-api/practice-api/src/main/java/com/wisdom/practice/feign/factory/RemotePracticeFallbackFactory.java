package com.wisdom.practice.feign.factory;

import com.wisdom.practice.feign.RemotePracticeService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@Slf4j
@Component
public class RemotePracticeFallbackFactory implements FallbackFactory<RemotePracticeService> {
    @Override
    public RemotePracticeService create(Throwable throwable) {
        return new RemotePracticeService() {
        };
    }
}
