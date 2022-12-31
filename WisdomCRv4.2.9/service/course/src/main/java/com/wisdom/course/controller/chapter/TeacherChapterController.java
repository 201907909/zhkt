package com.wisdom.course.controller.chapter;

import com.wisdom.base.util.R;
import com.wisdom.base.util.SnowflakeIdWorker;
import com.wisdom.chapter.bean.dto.ChapterAddDTO;
import com.wisdom.chapter.bean.dto.ChapterDeleteDTO;
import com.wisdom.chapter.bean.dto.ChapterUpdateIndexDTO;
import com.wisdom.chapter.bean.dto.ChapterUpdateInfoDTO;
import com.wisdom.course.service.TeacherChapterService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @author WisdomCR
 * @since 2022/11/1
 */
@RestController
@RequestMapping("/teacherChapter")
public class TeacherChapterController {
    @Autowired
    private TeacherChapterService teacherChapterService;

    @PostMapping("/createCourseOfChapterByTea")
    @ApiOperation(value = "教师为某课程创建章节")
    public R create(@Validated @RequestBody ChapterAddDTO chapterAddDTO) throws IOException {
        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(0, 0);
        chapterAddDTO.setChaId(snowflakeIdWorker.nextId());
        if (teacherChapterService.create(chapterAddDTO)) {
            return R.ok();
        }
        return R.error("创建失败");
    }

    @PostMapping("/delCourseChapterByTea")
    @ApiOperation(value = "教师删除某章节")
    public R deleteFa(@RequestBody ChapterDeleteDTO chapterDeleteDTO) {
        if (teacherChapterService.delete(chapterDeleteDTO)) {
            return R.ok();
        }
        return R.error("数据库没有此章节");
    }

    @GetMapping("/getAllCourseOfChapterInfoByTea")
    @ApiOperation(value = "教师查看所有章节")
    public R retrieveAll(@RequestParam Long cou_id) {
        return R.data(teacherChapterService.retrieveAll(cou_id));
    }

    @PostMapping("/reEditCourseOfChapterInfoByTea")
    @ApiOperation(value = "教师修改章节名称、地址")
    public R updateInfo(@Validated @RequestBody ChapterUpdateInfoDTO chapterUpdateInfoDTO) throws IOException {
        if (teacherChapterService.updateInfo(chapterUpdateInfoDTO)) {
            return R.ok();
        }
        return R.error();
    }

    @PostMapping("/reEditCourseOfChapterIndexOfFaByTea")
    @ApiOperation(value = "教师修改章节排列顺序，传入章节id、课程id、和排列顺序")
    public R updateIndexOfFa(@Validated @RequestBody List<ChapterUpdateIndexDTO> list) {
        if (teacherChapterService.updateIndex(list)) {
            return R.ok();
        }
        return R.error();
    }
}
