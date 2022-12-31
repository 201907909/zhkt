package com.wisdom.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wisdom.chapter.bean.vo.ChapterVO;
import com.wisdom.chapter.domain.Chapter;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ChapterMapper extends BaseMapper<Chapter> {
    List<ChapterVO> getClaCourseAllCha(@Param("couId") Long couId);
}
