package com.wisdom.comment.feign.factory;

import com.wisdom.comment.feign.RemoteCommentService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@Slf4j
@Component
public class RemoteCommentFallbackFactory implements FallbackFactory<RemoteCommentService> {
    @Override
    public RemoteCommentService create(Throwable throwable) {
        return new RemoteCommentService() {

        };
    }
}
