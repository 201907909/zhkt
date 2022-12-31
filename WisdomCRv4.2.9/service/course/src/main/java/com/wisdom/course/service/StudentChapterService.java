package com.wisdom.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wisdom.chapter.bean.vo.ChapterInfoVO;
import com.wisdom.chapter.domain.Chapter;

import java.util.List;

/**
 * @author WisdomCR
 * @since 2022/11/3
 */
public interface StudentChapterService extends IService<Chapter> {
    List<ChapterInfoVO> retrieveAll(Long cou_id);
}
