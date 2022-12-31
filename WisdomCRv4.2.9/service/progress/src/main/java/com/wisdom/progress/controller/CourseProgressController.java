package com.wisdom.progress.controller;

import com.wisdom.base.util.R;
import com.wisdom.progress.service.StuCourseProService;
import com.wisdom.user.bean.dto.AddStuCourseProDTO;
import com.wisdom.user.bean.dto.GetStuCourseProDTO;
import com.wisdom.user.bean.dto.GetStuLearningProDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;

/**
 * 学生购买课程的学习进度接口，对应数据库stu_cou_progress
 * @author WisdomCR
 * @since 2022/11/10
 */
@RestController
@RequestMapping("/courseProgress")
public class CourseProgressController {
    @Autowired
    private StuCourseProService stuCourseProService;

    @PostMapping("/updateCourseProgress")
    public R updateCourseProgress(@Validated @RequestBody AddStuCourseProDTO stuCourseProDTO, @ApiIgnore HttpSession httpSession) {
//        Long stuId = Long.valueOf(String.valueOf(httpSession.getAttribute("stuId")));
//        if (stuId == null) {
//            return R.error(404, "无访问权限");
//        }
//        stuCourseProDTO.setStuId(stuId);
        stuCourseProDTO.setStuId(Long.valueOf("1037775116978618368"));
        if (stuCourseProService.updateCourseProgress(stuCourseProDTO)) {
            return R.ok();
        }
        return R.error();
    }

    @GetMapping("/getCourseProgress")
    public R getCourseProgress(@Validated @RequestBody GetStuCourseProDTO getStuCourseProDTO, @ApiIgnore HttpSession httpSession) {
//        Long stuId = Long.valueOf(String.valueOf(httpSession.getAttribute("stuId")));
//        if (stuId == null) {
//            return R.error(404, "无访问权限");
//        }
        getStuCourseProDTO.setStuId(Long.valueOf("1037775116978618368"));
        return R.data(stuCourseProService.getCourseProgress(getStuCourseProDTO));
    }
}
