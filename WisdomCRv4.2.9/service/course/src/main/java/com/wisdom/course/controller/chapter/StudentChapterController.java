package com.wisdom.course.controller.chapter;

import com.wisdom.base.util.R;
import com.wisdom.course.service.StudentChapterService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WisdomCR
 * @since 2022/11/3
 */
@RestController
@RequestMapping("/studentChapter")
public class StudentChapterController {
    @Autowired
    private StudentChapterService studentChapterService;

    @GetMapping("/getAllCourseOfChapterInfoByTea")
    @ApiOperation(value = "学生查看所有章节")
    public R retrieveAll(@RequestParam Long cou_id) {
        return R.data(studentChapterService.retrieveAll(cou_id));
    }
}
