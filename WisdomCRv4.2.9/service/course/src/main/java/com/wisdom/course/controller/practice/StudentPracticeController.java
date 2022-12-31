package com.wisdom.course.controller.practice;

import com.wisdom.base.util.R;
import com.wisdom.course.service.StudentPracticeService;
import com.wisdom.practice.bean.dto.StuPracticeDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("studentPractice")
public class StudentPracticeController {
    @Resource
    private StudentPracticeService studentPracticeService;

    @GetMapping("/getAllPracticeSimpleInfo")
    @ApiOperation(value = "学生查看该课程的所有线下实践任务")
    public R getAllPracticeSimpleInfo(@RequestParam Long couId) {
        return studentPracticeService.getAllPracticeSimpleInfo(couId);
    }

    @GetMapping("/getPracticeSimpleInfo")
    @ApiOperation(value = "学生查看指定线下实践任务")
    public R getPracticeSimpleInfo(@RequestParam Long id, @ApiIgnore HttpSession httpSession) {
        Long stuId = (Long) httpSession.getAttribute("stuId");
        if (stuId == null) {
            return R.error(403, "无访问权限");
        }
        return studentPracticeService.getPracticeSimpleInfo(id, stuId);
    }

    @GetMapping("/getPracticeSimpleByInfo")
    @ApiOperation(value = "学生模糊查询线下实践任务")
    public R getPracticeSimpleByInfo(@RequestParam String info, @RequestParam Long couId) {
        return studentPracticeService.getPracticeSimpleByInfo(info, couId);
    }

    @PostMapping("/studentJoinPractice")
    @ApiOperation(value = "学生是否参加线下实践任务")
    public R studentIsJoinPractice(@Validated @RequestBody StuPracticeDTO stuPracticeDTO, @ApiIgnore HttpSession httpSession) {
        Long stuId = (Long) httpSession.getAttribute("stuId");
        if (stuId == null) {
            return R.error(403, "无访问权限");
        }
        stuPracticeDTO.setStuId(stuId);
        return studentPracticeService.studentIsJoinPractice(stuPracticeDTO);
    }

    @PostMapping("/studentSubmitPractice")
    @ApiOperation(value = "学生提交线下实践任务")
    public R studentSubmitPractice(@Validated @RequestBody StuPracticeDTO stuPracticeDTO, @ApiIgnore HttpSession httpSession) {
        Long stuId = (Long) httpSession.getAttribute("stuId");
        if (stuId == null) {
            return R.error(403, "无访问权限");
        }
        stuPracticeDTO.setStuId(stuId);
        return studentPracticeService.studentSubmitPractice(stuPracticeDTO);
    }
}
