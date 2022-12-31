package com.wisdom.stclass.feign.factory;

import com.wisdom.stclass.feign.RemoteClassService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@Slf4j
@Component
public class RemoteClassFallbackFactory implements FallbackFactory<RemoteClassService> {
    @Override
    public RemoteClassService create(Throwable throwable) {
        return new RemoteClassService() {
            @Override
            public Map<String,Object> getStuDailyTime(Long stuId) {
                return null;
            }
            @Override
            public List<String> getStuClockDate(Long stuId) {
                return null;
            }
            @Override
            public Integer getStuContinuedClock(Long stuId) {
                return null;
            }
        };
    }
}
