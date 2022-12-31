package com.wisdom.course.service.impl;

import com.wisdom.chapter.bean.vo.ChapterVO;
import com.wisdom.course.mapper.ChapterMapper;
import com.wisdom.course.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChapterServiceImpl implements ChapterService {

    @Autowired
    private ChapterMapper chapterMapper;

    @Override
    public List<ChapterVO> getClaCourseAllCha(Long couId) {
        return chapterMapper.getClaCourseAllCha(couId);

    }
}
