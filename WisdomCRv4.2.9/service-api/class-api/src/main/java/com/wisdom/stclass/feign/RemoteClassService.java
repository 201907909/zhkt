package com.wisdom.stclass.feign;

import com.wisdom.stclass.feign.factory.RemoteClassFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Map;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@FeignClient(name = "class",value = "class",fallbackFactory = RemoteClassFallbackFactory.class)
public interface RemoteClassService {
    @GetMapping("/class/getStuDailyTime")
    Map<String,Object> getStuDailyTime(@RequestParam(value = "stuId") Long stuId);
    @GetMapping("/class/getStuClockDate")
    List<String> getStuClockDate(@RequestParam(value = "stuId") Long stuId);
    @GetMapping("/class/getStuContinuedClock")
    Integer getStuContinuedClock(@RequestParam(value = "stuId") Long stuId);
}
