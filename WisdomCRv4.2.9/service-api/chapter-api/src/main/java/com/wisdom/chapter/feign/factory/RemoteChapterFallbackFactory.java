package com.wisdom.chapter.feign.factory;

import com.wisdom.chapter.bean.vo.ChapterVO;
import com.wisdom.chapter.feign.RemoteChapterService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@Slf4j
@Component
public class RemoteChapterFallbackFactory implements FallbackFactory<RemoteChapterService> {
    @Override
    public RemoteChapterService create(Throwable throwable) {
        return new RemoteChapterService() {
            @Override
            public List<ChapterVO> getClaCourseAllCha(Long couId) {
                return null;
            }
        };
    }
}
