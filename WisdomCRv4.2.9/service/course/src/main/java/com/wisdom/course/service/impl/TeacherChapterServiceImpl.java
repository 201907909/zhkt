package com.wisdom.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wisdom.chapter.bean.dto.ChapterAddDTO;
import com.wisdom.chapter.bean.dto.ChapterDeleteDTO;
import com.wisdom.chapter.bean.dto.ChapterUpdateIndexDTO;
import com.wisdom.chapter.bean.dto.ChapterUpdateInfoDTO;
import com.wisdom.chapter.bean.vo.ChapterInfoVO;
import com.wisdom.chapter.domain.Chapter;
import com.wisdom.course.mapper.ChapterMapper;
import com.wisdom.course.service.TeacherChapterService;
import com.wisdom.course.utils.getUrlTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class TeacherChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements TeacherChapterService {
    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private getUrlTime getUrlTime;

    /**
     * 教师创建章节
     *
     * @param chapterAddDTO 章节实体类
     * @return {@link Boolean}
     */
    @Override
    public Boolean create(ChapterAddDTO chapterAddDTO) throws IOException {
        Long sec = getUrlTime.mp4(chapterAddDTO.getChaUrl());
        if (sec != null) {
            Chapter chapter = new Chapter();
            chapter.setChaId(chapterAddDTO.getChaId())
                    .setCouId(chapterAddDTO.getCouId())
                    .setFaChaId(chapterAddDTO.getFaChaId())
                    .setChaIndex(chapterAddDTO.getChaIndex())
                    .setChaTitle(chapterAddDTO.getChaTitle())
                    .setChaUrl(chapterAddDTO.getChaUrl())
                    .setChaTime(sec);
            int result = chapterMapper.insert(chapter);
            if (result == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 教师删除章节
     *
     * @param chapterDeleteDTO 教师删除章节DTO对象
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)// 指定回滚,遇到异常Exception时回滚
    public Boolean delete(ChapterDeleteDTO chapterDeleteDTO) {
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("cha_id", chapterDeleteDTO.getChaId());
            queryWrapper.eq("cou_id", chapterDeleteDTO.getCouId());
            queryWrapper.select("cha_index");
            Chapter chapter = chapterMapper.selectOne(queryWrapper);// 获取被删的数据项的排列序号
            Integer index = chapter.getChaIndex();
            int result = chapterMapper.delete(queryWrapper);
            if (result == 1) {
                queryWrapper.clear();
                queryWrapper.eq("cou_id", chapterDeleteDTO.getCouId());
                queryWrapper.gt("cha_index", index);
                queryWrapper.select("cha_id");
                queryWrapper.orderByAsc("cha_index");
                List<Chapter> chapters = chapterMapper.selectList(queryWrapper);// 获取被删的后面还有多少条数据
                UpdateWrapper updateWrapper = new UpdateWrapper();
                for (int i = 0; i < chapters.size(); i++) {
                    updateWrapper.eq("cha_id", chapters.get(i).getChaId());
                    updateWrapper.eq("cou_id", chapterDeleteDTO.getCouId());
                    updateWrapper.set("cha_index", index+i);
                    chapterMapper.update(null, updateWrapper);
                    updateWrapper.clear();
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 回滚
            throw new RuntimeException();
        }
        return false;
    }

    /**
     * 教师查询所有章节
     *
     * @param cou_id 课程id
     * @return {@link List}<{@link Chapter}>
     */
    @Override
    public List<ChapterInfoVO> retrieveAll(Long cou_id) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("cou_id", cou_id);
        //wrapper.eq("fa_cha_id", 0); // 展示父章节信息
        wrapper.select("id", "cou_id", "cha_id", "cha_title", "cha_index", "cha_url", "cha_time");
        wrapper.orderByAsc("cha_index");
        List<ChapterInfoVO> chapterOfFatherVO = new ArrayList<>();
        List<Map<String, String>> list = chapterMapper.selectMaps(wrapper);
        for (int i = 0; i < list.size(); i++) {
            ChapterInfoVO temp = new ChapterInfoVO();
            temp.setId(Long.valueOf(String.valueOf(list.get(i).get("id"))))
                    .setCouId(String.valueOf(list.get(i).get("cou_id")))
                    .setChaId(String.valueOf(list.get(i).get("cha_id")))
                    .setChaIndex(Integer.valueOf(String.valueOf(list.get(i).get("cha_index"))))
                    .setChaTitle(String.valueOf(list.get(i).get("cha_title")))
                    .setChaUrl(String.valueOf(list.get(i).get("cha_url")))
                    .setChaTime(Long.valueOf(String.valueOf(list.get(i).get("cha_time"))));
            chapterOfFatherVO.add(i, temp);
        }
        return chapterOfFatherVO;
    }

    /**
     * 更新章节信息
     *
     * @param chapterUpdateInfoDTO 修改章节名称地址DTO类
     * @return {@link Boolean}
     */
    @Override
    public Boolean updateInfo(ChapterUpdateInfoDTO chapterUpdateInfoDTO) throws IOException {
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("id", chapterUpdateInfoDTO.getId());
        if (chapterUpdateInfoDTO.getChaTitle() != null && chapterUpdateInfoDTO.getChaTitle() != "") {
            wrapper.set("cha_title", chapterUpdateInfoDTO.getChaTitle());
        }
        if (chapterUpdateInfoDTO.getChaUrl() != null && chapterUpdateInfoDTO.getChaUrl() != "") {
            wrapper.set("cha_url", chapterUpdateInfoDTO.getChaUrl());
            Long sec = getUrlTime.mp4(chapterUpdateInfoDTO.getChaUrl());
            if (sec == null) {
                return false;
            }
            wrapper.set("cha_time", sec);
        }
        int result = chapterMapper.update(null, wrapper);
        if (result == 1) {
            return true;
        }
        return false;
    }

    /**
     * 更新章节排列顺序
     *
     * @param list 章节实体类列表
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateIndex(List<ChapterUpdateIndexDTO> list) {
        try {
            UpdateWrapper wrapper = new UpdateWrapper();
            for (int i = 0; i < list.size(); i++) {
                wrapper.eq("cou_id", list.get(i).getCouId());
                wrapper.eq("cha_id", list.get(i).getChaId());
                wrapper.eq("fa_cha_id", 0);
                wrapper.set("cha_index", list.get(i).getChaIndex());
                chapterMapper.update(null, wrapper);
                wrapper.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return true;
    }
}
