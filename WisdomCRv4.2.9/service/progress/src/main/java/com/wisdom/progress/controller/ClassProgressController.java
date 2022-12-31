package com.wisdom.progress.controller;

import com.wisdom.base.util.R;
import com.wisdom.progress.service.StuLearningProService;
import com.wisdom.user.bean.dto.AddStuLearningProDTO;
import com.wisdom.user.bean.dto.GetStuLearningProDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;

/**
 * 学生在班级的学习进度接口，对应数据库learning_progress
 * @author WisdomCR
 * @since 2022/11/10
 */
@RestController
@RequestMapping("/classProgress")
public class ClassProgressController {
    @Autowired
    private StuLearningProService stuLearningProService;

    @PostMapping("/updateLearningProgress")
    public R updateLearningProgress(@Validated @RequestBody AddStuLearningProDTO stuLearningProDTO, @ApiIgnore HttpSession httpSession) {
//        Long stuId = Long.valueOf(String.valueOf(httpSession.getAttribute("stuId")));
//        if (stuId == null) {
//            return R.error(404, "无访问权限");
//        }
//        stuLearningProDTO.setStuId(stuId);
        stuLearningProDTO.setStuId(Long.valueOf("1037775116978618368"));
        if (stuLearningProService.updateLearningProgress(stuLearningProDTO)) {
            return R.ok();
        }
        return R.error();
    }

    @GetMapping("/getClassProgress")
    public R getClassProgress(@Validated @RequestBody GetStuLearningProDTO getStuLearningProDTO, @ApiIgnore HttpSession httpSession) {
//        Long stuId = Long.valueOf(String.valueOf(httpSession.getAttribute("stuId")));
//        if (stuId == null) {
//            return R.error(404, "无访问权限");
//        }
        getStuLearningProDTO.setStuId(Long.valueOf("1037775116978618368"));
        return R.data(stuLearningProService.getClassProgress(getStuLearningProDTO));
    }
}
