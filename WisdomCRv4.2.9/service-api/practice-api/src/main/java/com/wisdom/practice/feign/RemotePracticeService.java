package com.wisdom.practice.feign;

import com.wisdom.practice.feign.factory.RemotePracticeFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@FeignClient(name = "course",fallbackFactory = RemotePracticeFallbackFactory.class)
public interface RemotePracticeService {

}
