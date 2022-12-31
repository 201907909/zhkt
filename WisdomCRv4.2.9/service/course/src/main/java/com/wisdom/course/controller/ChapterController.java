package com.wisdom.course.controller;

import com.wisdom.chapter.bean.vo.ChapterVO;
import com.wisdom.course.service.ChapterService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chapter")
@CrossOrigin
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @ApiOperation("传couId返回课程下的章节集合")
    @GetMapping("/getClaCourseAllCha")
    public List<ChapterVO> getClaCourseAllCha(@RequestParam("couId") Long couId) {
        return chapterService.getClaCourseAllCha(couId);
    }

}
