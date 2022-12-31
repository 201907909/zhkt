package com.wisdom.comment.feign;

import com.wisdom.comment.feign.factory.RemoteCommentFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@FeignClient(value = "interaction", name = "interaction",fallbackFactory = RemoteCommentFallbackFactory.class)
public interface RemoteCommentService {

}
