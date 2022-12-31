package com.wisdom.durations.feign;

import com.wisdom.durations.feign.factory.RemoteDurationsFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@FeignClient(value = "course", name = "course",fallbackFactory = RemoteDurationsFallbackFactory.class)
public interface RemoteDurationsService {

}
