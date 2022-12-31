package com.wisdom.course.service;

import com.wisdom.chapter.bean.vo.ChapterVO;

import java.util.List;

public interface ChapterService {

    List<ChapterVO> getClaCourseAllCha(Long couId);
}
