package com.wisdom.type.feign;

import com.wisdom.type.feign.factory.RemoteTypeFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@FeignClient(value = "course", name = "course",fallbackFactory = RemoteTypeFallbackFactory.class)
public interface RemoteTypeService {

}
