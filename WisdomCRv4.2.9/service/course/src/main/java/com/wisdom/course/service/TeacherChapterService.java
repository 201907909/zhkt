package com.wisdom.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wisdom.chapter.bean.dto.ChapterAddDTO;
import com.wisdom.chapter.bean.dto.ChapterDeleteDTO;
import com.wisdom.chapter.bean.dto.ChapterUpdateIndexDTO;
import com.wisdom.chapter.bean.dto.ChapterUpdateInfoDTO;
import com.wisdom.chapter.bean.vo.ChapterInfoVO;
import com.wisdom.chapter.domain.Chapter;

import java.io.IOException;
import java.util.List;

/**
 * @author WisdomCR
 * @since 2022/11/1
 */
public interface TeacherChapterService extends IService<Chapter> {
    Boolean create(ChapterAddDTO chapterAddDTO) throws IOException;
    Boolean delete(ChapterDeleteDTO chapterDeleteDTO);
    List<ChapterInfoVO> retrieveAll(Long cou_id);
    Boolean updateInfo(ChapterUpdateInfoDTO ChapterUpdateInfoDTO) throws IOException;
    Boolean updateIndex(List<ChapterUpdateIndexDTO> list);
}
