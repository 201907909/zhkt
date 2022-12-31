package com.wisdom.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wisdom.chapter.bean.vo.ChapterInfoVO;
import com.wisdom.chapter.domain.Chapter;
import com.wisdom.course.mapper.ChapterMapper;
import com.wisdom.course.service.StudentChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author WisdomCR
 * @since 2022/11/3
 */
@Service
public class StudentChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements StudentChapterService {
    @Autowired
    private ChapterMapper chapterMapper;

    /**
     * 学生查询所有父章节
     *
     * @param cou_id 课程id
     * @return {@link List}<{@link Chapter}>
     */
    @Override
    public List<ChapterInfoVO> retrieveAll(Long cou_id) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("cou_id", cou_id);
        wrapper.select("id", "cou_id", "cha_id", "cha_title", "cha_index", "cha_url", "cha_time");
        wrapper.orderByAsc("cha_index");
        List<ChapterInfoVO> chapterOfFatherVO = new ArrayList<>();
        List<Map<String, String>> list = chapterMapper.selectMaps(wrapper);
        for (int i = 0; i < list.size(); i++) {
            ChapterInfoVO temp = new ChapterInfoVO();
            temp.setChaId(String.valueOf(list.get(i).get("cha_id")));
            temp.setChaIndex(Integer.valueOf(String.valueOf(list.get(i).get("cha_index"))));
            temp.setChaTitle(String.valueOf(list.get(i).get("cha_title")));
            chapterOfFatherVO.add(i, temp);
        }
        return chapterOfFatherVO;
    }
}
