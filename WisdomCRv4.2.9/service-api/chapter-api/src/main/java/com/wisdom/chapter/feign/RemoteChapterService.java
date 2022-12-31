package com.wisdom.chapter.feign;

import com.wisdom.chapter.bean.vo.ChapterVO;
import com.wisdom.chapter.feign.factory.RemoteChapterFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@FeignClient(name = "course", value = "course",fallbackFactory = RemoteChapterFallbackFactory.class)
public interface RemoteChapterService {
    /**
     * 传stuId,couId,classId返回班级课程课程下的章节集合
     */
    @GetMapping("/chapter/getClaCourseAllCha")
    List<ChapterVO> getClaCourseAllCha(@RequestParam("couId") Long couId);
}
