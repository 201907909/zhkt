package com.wisdom.progress.controller;

import com.wisdom.base.util.DateUtils;
import com.wisdom.base.util.R;
import com.wisdom.durations.bean.dto.CouCommentDTO;
import com.wisdom.durations.bean.dto.ProgressDTO;
import com.wisdom.progress.service.ProgressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/progress")
@Api(tags = "学习进度管理接口")
@CrossOrigin
public class ProgressController {
    @Resource
    private ProgressService progressService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

//    @ApiOperation(value = "新增班级课程学习记录，每次结束观看记录结束，观看结束就记录")
//    @PostMapping("/addClasCouProgress")
//    public R addClasCouProgress(@RequestBody ProgressDTO progressDTO) {
//        return R.data(progressService.addClasCouProgress(progressDTO));
//    }

//    @ApiOperation(value = "修改班级课程学习记录，每次结束观看记录结束，观看结束就记录")
//    @PostMapping("/updateClasCouProgress")
//    public R updateClasCouProgress(@RequestBody ProgressDTO progressDTO) {
//        progressService.updateClasCouProgress(progressDTO);
//        return R.ok("观看视频时长修改成功");
//    }

    @ApiOperation(value = "新增课程学习记录，每次结束观看记录结束，观看结束就记录")
    @PostMapping("/addCouProgress")
    public R addCouProgress(@RequestBody ProgressDTO progressDTO) {
        progressService.addCouProgress(progressDTO);
        return R.ok("新增学习记录成功");
    }

//    @ApiOperation(value = "修改自选课程学习记录，每次结束观看记录结束，观看结束就记录")
//    @PostMapping("/updateCouProgress")
//    public R updateCouProgress(@RequestBody ProgressDTO progressDTO) {
//        progressService.updateCouProgress(progressDTO);
//        return R.ok("观看视频时长修改成功");
//    }

    @ApiOperation(value = "新增学生课程历史学习记录")
    @PostMapping("/addHistoryLearningDuration")
    public R addHistoryLearningDuration(@RequestBody ProgressDTO progressDTO) {
        return R.data(progressService.addHistoryLearningDuration(progressDTO));
    }

    @ApiOperation(value = "修改学生课程历史学习记录")
    @PostMapping("/upHistoryLearningDuration")
    public R upHistoryLearningDuration(@RequestBody ProgressDTO progressDTO) {
        progressService.upHistoryLearningDuration(progressDTO);
        return R.ok("观看视频时长修改成功");
    }

    @ApiOperation(value = "查看班级课程的学习进度")
    @GetMapping("/getClasCouProgress")
    public R getClasCouProgress(@ApiIgnore HttpSession session) {
        Long stuId = (Long) session.getAttribute("stuId");
        if (stuId == null) {
            return R.error(403, "无访问权限");
        }
        return R.data(progressService.getClasCouProgress(stuId));
    }

    @ApiOperation(value = "班级课程进度详情")
    @GetMapping("/clasCourseDetail")
    public R claCourseDetail(@RequestParam("couId") String couId,
                             @RequestParam("classId") String classId,
                             @ApiIgnore HttpSession session){
        Long stuId = (Long) session.getAttribute("stuId");
        if (stuId == null) {
            return R.error(403, "无访问权限");
        }
        return R.data(progressService.claCourseDetail(stuId, Long.valueOf(classId), Long.valueOf(couId)));
    }

    @ApiOperation(value = "查看自选课程的学习进度")
    @GetMapping("/getCouProgress")
    public R getCouProgress(@ApiIgnore HttpSession session){
        Long stuId = (Long) session.getAttribute("stuId");
        if (stuId == null) {
            return R.error(403, "无访问权限");
        }
        return R.data(progressService.getCouProgress(stuId));
    }

    @ApiOperation(value = "自选课程进度详情")
    @GetMapping("/courseDetail")
    public R courseDetail(@RequestParam("couId") String couId,
                          @ApiIgnore HttpSession session){
        Long stuId = (Long) session.getAttribute("stuId");
        if (stuId == null) {
            return R.error(403, "无访问权限");
        }
        return R.data(progressService.courseDetail(stuId, Long.valueOf(couId)));
    }

    @ApiOperation(value = "查看课程评论")
    @GetMapping("/getCouCommment")
    public R getCouCommment(@RequestParam("couId") String couId,
                            @ApiIgnore HttpSession session){
        Long stuId = (Long) session.getAttribute("stuId");
        if (stuId == null) {
            return R.error(403, "无访问权限");
        }
        return R.data(progressService.getCouCommment(Long.valueOf(couId), stuId));
    }

    @ApiOperation(value = "发表评论")
    @PostMapping("/addComment")
    public R addComment(@RequestBody CouCommentDTO couCommentDTO) {
        return progressService.addComment(couCommentDTO);
    }

    @ApiOperation(value = "点赞评论")
    @PostMapping("/click")
    public R click(@RequestParam(value = "couComId") String couComId,
                   @ApiIgnore HttpSession session) {
        Long stuId = (Long) session.getAttribute("stuId");
        if (stuId == null) {
            return R.error(403 , "无访问权限");
        }
        progressService.click(Long.valueOf(couComId), stuId);
        return R.ok("成功");
    }

    @ApiOperation(value = "下踩评论")
    @PostMapping("/drop")
    public R drop(@RequestParam(value = "couComId") String couComId,
                  @ApiIgnore HttpSession session){
        Long stuId = (Long) session.getAttribute("stuId");
        if (stuId == null) {
            return R.error(403 , "无访问权限");
        }
        progressService.drop(Long.valueOf(couComId), stuId);
        return R.ok("成功");
    }

    @ApiOperation("昨天的学习时长")
    @GetMapping("/yStudyTime")
    public R yStudyTime(@ApiIgnore HttpSession session){
        Long stuId = (Long) session.getAttribute("stuId");
        return R.data(progressService.yStudyTime(stuId));
    }

    @ApiOperation("今日的学习时长")
    @GetMapping("/tStudyTime")
    public R tStudyTime(@ApiIgnore HttpSession session) {
        String stuId = session.getAttribute("stuId").toString();
        if (stuId == null) {
            return R.error(403, "无访问权限");
        }
        return R.data(progressService.tStudyTime(stuId));
    }

    @ApiOperation("上个月的学习时长")
    @GetMapping("/monthTime")
    public R monthTime(@ApiIgnore HttpSession session){
        Long stuId = (Long) session.getAttribute("stuId");
        return R.data(progressService.monthTime(stuId));
    }

    @ApiOperation("章节学习进度")
    @GetMapping("/chapterProgress")
    public R chapterProgress(@ApiIgnore HttpSession session,
                             @RequestParam("chaId") String chaId,
                             @RequestParam(value = "classId", required = false, defaultValue = "") String classId) {
        Long stuId = (Long) session.getAttribute("stuId");
        Long cId = 0L;
        if (classId != null && !classId.equals("")) {
            cId = Long.valueOf(classId);
        }
        return R.data(progressService.chapterProgress(stuId, Long.valueOf(chaId), cId));
    }

    @ApiOperation("最新章节学习进度")
    @GetMapping("/newChapterProgress")
    public R newChapterProgress(@ApiIgnore HttpSession session,
                                @RequestParam("couId") String couId,
                                @RequestParam(value = "classId", required = false, defaultValue = "") String classId) {
        Long stuId = (Long) session.getAttribute("stuId");
        Long cId = 0L;
        if (classId != null && !classId.equals("")) {
            cId = Long.valueOf(classId);
        }
        return R.data(progressService.newChapterProgress(stuId, Long.valueOf(couId), cId));
    }

    @GetMapping("/cleanTtime")
    public void cleanTtime(@RequestParam("stuId") String stuId) {
        stringRedisTemplate.delete(DateUtils.getDate()+stuId);
    }

    @ApiOperation("看完章节加积分")
    @GetMapping("/addScore")
    public R addScore(@ApiIgnore HttpSession session, @RequestParam("scoreType") int scoreType) {
        Long stuId = (Long) session.getAttribute("stuId");
        if (stuId == null) {
            return R.error(403 , "无访问权限");
        }
        progressService.addScore(stuId, scoreType);
        return R.ok();
    }
}
