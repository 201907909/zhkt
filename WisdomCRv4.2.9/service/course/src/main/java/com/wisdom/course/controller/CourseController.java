package com.wisdom.course.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisdom.base.util.R;
import com.wisdom.base.util.SnowflakeIdWorker;
import com.wisdom.chapter.bean.vo.ChapterVO;
import com.wisdom.course.bean.dto.*;
import com.wisdom.course.bean.vo.*;
import com.wisdom.course.service.CourseService;
import com.wisdom.course.service.impl.QiniuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * @author WisdomCR
 * @since  2022/10/18
 */
@RestController
@RequestMapping("/course")
@Api(tags = "课程接口")
@CrossOrigin
public class CourseController {

    @Resource
    private CourseService courseService;

    @Resource
    private QiniuService qiniuService;


//    @SneakyThrows
//    @PostMapping("/courseFile")
//    @ApiOperation(value = "上传图片")
//    public R courseFile(@RequestParam(value = "multipartFile") MultipartFile multipartFile, @ApiIgnore HttpSession session) {
//        Long teaId = (Long) session.getAttribute("teaId");
//        if (teaId == null) {
//            return R.error(403, "无访问权限");
//        }
//        return courseService.courseFile(multipartFile);
//    }

    @PostMapping("/deletePicture")
    @ApiOperation("删除图片")
    public void deletePicture(@RequestParam("oldImg") String oldImg){
        if(oldImg!=null) {
            String[] path = oldImg.split("/");
            String key = path[3];
            try {
                qiniuService.deleteFile(key);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @SneakyThrows
    @PostMapping("/courseFile")
    @ApiOperation(value = "上传图片")
    public R courseFile(@RequestParam(value = "multipartFile") MultipartFile multipartFile, @ApiIgnore HttpSession session){
        Long sessionId = (Long) session.getAttribute("sessionId");
        if (sessionId == null){
            return R.error(403, "无访问权限");
        }
        if(multipartFile.isEmpty()) {
            return R.error();
        }

        try {
            String fileUrl=qiniuService.saveImage(multipartFile);
            return R.data(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.error("fail");
    }

    @PostMapping("/addCourse")
    @ApiOperation(value = "新增课程")
    public R addCourse(@Validated @RequestBody CourseAddDTO courseAddDTO, @ApiIgnore HttpSession httpSession) {
        Long teaId = (Long) httpSession.getAttribute("teaId");
        if (teaId == null) {
            return R.error(403, "无访问权限");
        }
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        courseAddDTO.setCouId(idWorker.nextId())
                .setTeaId(teaId);
        return courseService.addCourse(courseAddDTO);
    }

    @GetMapping("/deleteCourseById")
    @ApiOperation(value = "删除课程")
    public R deleteCourseById(@RequestParam(value = "id") Long id, @ApiIgnore HttpSession session) {
        Long teaId = (Long) session.getAttribute("teaId");
        if (teaId == null) {
            return R.error(403, "无访问权限");
        }
        if (courseService.deleteCourseById(id)) {
            return R.ok("删除班级成功");
        } else {
            return R.error("删除班级失败");
        }
    }

    @PostMapping("/updateCourseById")
    @ApiOperation(value = "修改课程信息")
    public R updateClassById(@Validated @RequestBody CourseUpdateDTO classUpdateDTO, @ApiIgnore HttpSession session) {
        Long teaId = (Long) session.getAttribute("teaId");
        if (teaId == null) {
            return R.error(403, "无访问权限");
        }
        return courseService.updateCourseById(classUpdateDTO);
    }

    // 获取该班级的所有课程，课程信息简略
    @GetMapping("/selectCourseByClassId")
    @ApiOperation(value="根据班级ID查询所有课程(课程)")
    public List<CourseBriefVO> selectCourseByClassId(Long classId){
        return courseService.selectCourseByClassId(classId);
    }

    // 获取系统所有课程，课程信息简略
    @PostMapping("/selectAllCourse")
    @ApiOperation(value="查询所有课程")
    public List<CourseBriefVO> selectAllCourse(@RequestParam("startIndex") Integer startIndex, @RequestParam("pageSize") Integer pageSize, @RequestParam("classId") Long classId){
        return courseService.selectAllCourse(startIndex, pageSize, classId);
    }

    @GetMapping("/selectAllCourseComplete")
    @ApiOperation(value="查询完整所有课程")
    public List<CourseBriefVO> selectAllCourseComplete(){
        return courseService.selectAllCourseComplete();
    }

    @GetMapping("/teaQueryAllCourse")
    @ApiOperation(value = "教师查询所有课程")
    public R teaQueryAllCourse(@ApiIgnore HttpSession session) {
        Long teaId = (Long) session.getAttribute("teaId");
        if (teaId == null) {
            return R.error(403, "无访问权限");
        }
        return courseService.teaQueryAllCourse(teaId);
    }

    @GetMapping("/queryCourseByClassId")
    @ApiOperation(value = "根据班级ID查询所有课程(学生)")
    public List<CourseAboutVO> queryCourseByClassId(Long classId) {
        return courseService.queryCourseByClassId(classId);
    }

    @GetMapping("/getCourseByCouId")
    @ApiOperation(value = "根据课程ID批量查询课程")
    public List<CourseVO> getCourseByCouId(@RequestBody List<Long> couId) {
        return courseService.getCourseByCouId(couId);
    }

    @PostMapping("/getCourPriceByCouId")
    List<Integer> getCourPriceByCouId(@RequestBody List<Long> couId) {
        return courseService.getCourPriceByCouId(couId);
    }


    @GetMapping("/courseDetailByCouId")
    @ApiOperation(value = "根据课程ID获取课程详情")
    public CourseVO courseDetailByCouId(@RequestParam("couId") Long couId) {
        return courseService.courseDetailByCouId(couId);
    }

    @GetMapping("/getChaIdsByCouId")
    @ApiOperation(value = "根据课程ID批量获取课程的章节ID")
    public List<Long> getChaIdsByCouId(@RequestParam("couId") Long couId) {
        return courseService.getChaIdsByCouId(couId);
    }

    @GetMapping("/getCourseByStuId")
    @ApiOperation(value = "根据学生ID获取该学生所有个人课程")
    public R getCourseByStuId(@RequestParam("stuId") Long stuId) {
        return R.data(courseService.getCourseByStuId(stuId));
    }

    @GetMapping("/classQueryAllCourse")
    @ApiOperation(value = "查看所有课程")
    public R classQueryAllCourse(@ApiIgnore HttpSession session) {
        Long teaId = (Long) session.getAttribute("teaId");
        if (teaId == null) {
            return R.error(403, "无访问权限");
        }
        List<CouAboutClassVO> couAboutClassVOS = courseService.classQueryAllCourse();
        if (couAboutClassVOS != null) {
            return R.data(couAboutClassVOS, "查询所有课程成功");
        } else {
            return R.error("查询所有课程失败");
        }
    }

    @PostMapping("updateCouCataNumById")
    @ApiOperation(value = "修改课程章节数")
    public Boolean updateCouCataNumById(@RequestParam("id") Long id) {
        return courseService.updateCouCataNumById(id);
    }

    @PostMapping("updateCouCollNumById")
    @ApiOperation(value = "修改课程收藏数")
    public Boolean updateCouCollNumById(@Validated @RequestBody CouInteractionUpDTO couInteractionUpDTO) {
        return courseService.updateCouCollNumById(couInteractionUpDTO);
    }

    @PostMapping("updateCartState")
    @ApiOperation(value = "修改购物车状态")
    public Boolean updateCartState(@Validated @RequestBody CouInteractionUpDTO couInteractionUpDTO) {
        return courseService.updateCartState(couInteractionUpDTO);
    }

    @PostMapping("updateCouShareNumById")
    @ApiOperation(value = "修改课程分享数")
    public Boolean updateCouShareNumById(@Validated @RequestBody CouInteractionUpDTO couInteractionUpDTO) {
        return courseService.updateCouShareNumById(couInteractionUpDTO);
    }

    @PostMapping("updateCouClicksById")
    @ApiOperation(value = "增加课程点击量")
    public Boolean updateCouClicksById(@RequestParam("id") Long id) {
        return courseService.updateCouClicksById(id);
    }

    @PostMapping("updateCouRecommendNumById")
    @ApiOperation(value = "增加课程点赞量")
    public Boolean updateCouRecommendNumById(@Validated @RequestBody CouInteractionUpDTO couInteractionUpDTO) {
        return courseService.updateCouRecommendNumById(couInteractionUpDTO);
    }

    @PostMapping("updateCouPurchaseById")
    @ApiOperation(value = "增加课程购买数")
    public Boolean updateCouPurchaseById(@Validated @RequestBody CouInteractionUpDTO couInteractionUpDTO) {
        return courseService.updateCouPurchaseById(couInteractionUpDTO);
    }

    @PostMapping("isCouPurchaseById")
    @ApiOperation(value = "课程是否已购买")
    public Boolean isCouPurchaseById(@Validated @RequestBody CouInteractionUpDTO couInteractionUpDTO) {
        return courseService.isCouPurchaseById(couInteractionUpDTO);
    }

    @PostMapping("updateEvaluateNumById")
    @ApiOperation(value = "修改课程评价数")
    public Boolean updateEvaluateNumById(@Validated @RequestBody CouInteractionUpDTO couInteractionUpDTO) {
        return courseService.updateEvaluateNumById(couInteractionUpDTO);
    }

    @GetMapping("/getCouPrice")
    @ApiOperation(value = "获得课程的价格")
    Integer getCouPrice(@RequestParam("couId") Long couId) {
        return courseService.getCouPrice(couId);
    }

    @GetMapping("/getCouIntroduction")
    @ApiOperation(value = "获得课程的介绍")
    String getCouIntroduction(@RequestParam("couId") Long couId) {
        return courseService.getCouIntroduction(couId);
    }

    @GetMapping("/recommendCourse")
    @ApiOperation(value = "推荐课程")
    public R recommendCourse(@ApiIgnore HttpSession session) {
        Long stuId = (Long) session.getAttribute("stuId");
        return R.data(courseService.recommendCourse(stuId));
    }

    @GetMapping("/recommendType")
    @ApiOperation(value = "推荐类型")
    public R recommendType() {
        return R.data(courseService.recommendType());
    }


    @GetMapping("/courseDetail")
    @ApiOperation(value = "课程详情")
    public R courseDetail(@RequestParam("couId") String couId, @ApiIgnore HttpSession session) {
        Long stuId = (Long) session.getAttribute("stuId");
        return R.data(courseService.courseDetail(stuId, Long.valueOf(couId)));
    }
    @GetMapping("/courseDetail1")
    @ApiOperation(value = "课程详情1")
    public R courseDetail1(@RequestParam("couId") String couId, @ApiIgnore HttpSession session) {
        Long stuId = (Long) session.getAttribute("stuId");
        return R.data(courseService.courseDetail1(stuId, Long.valueOf(couId)));
    }
    @GetMapping("/courseDetail2")
    @ApiOperation(value = "课程详情2")
    public R courseDetail2(@RequestParam("couId") String couId, @ApiIgnore HttpSession session) {
        Long stuId = (Long) session.getAttribute("stuId");
        return R.data(courseService.courseDetail2(stuId, Long.valueOf(couId)));
    }
    @GetMapping("/courseDetail3")
    @ApiOperation(value = "课程详情3")
    public R courseDetail3(@RequestParam("couId") String couId, @ApiIgnore HttpSession session) {
        Long stuId = (Long) session.getAttribute("stuId");
        return R.data(courseService.courseDetail3(stuId, Long.valueOf(couId)));
    }

    @GetMapping("/allChapterByCouId")
    @ApiOperation(value = "查看课程所有章节")
    public R allChapterByCouId(@RequestParam("couId") String couId) {
        return R.data(courseService.allChapterByCouId(Long.valueOf(couId)));
    }

    @GetMapping("search")
    @ApiOperation(value = "搜索课程")
    public R search(@RequestParam("val") String val, @RequestParam("current") Long current) {
        IPage<CouVO> page = new Page<>(current, 10);
        return R.data(courseService.search(val, page));
    }

    @ApiOperation("传couId返回课程下的章节集合")
    @GetMapping("/getClaCourseAllCha")
    public List<ChapterVO> getClaCourseAllCha(@RequestParam("couId") Long couId) {
        return courseService.getClaCourseAllCha(couId);
    }

    @PostMapping("/click")
    @ApiOperation(value = "课程点赞")
    public R click(@ApiIgnore HttpSession session,
                   @RequestParam("couId") String couId) {
        Long stuId = (Long) session.getAttribute("stuId");
        if (stuId == null) {
            return R.error("无法访问权限");
        }
        return R.data(courseService.click(stuId, Long.valueOf(couId)));
    }

    @PostMapping("/cancelClick")
    @ApiOperation(value = "课程取消点赞")
    public R cancelClick(@ApiIgnore HttpSession session,
                   @RequestParam("couId") String couId) {
        Long stuId = (Long) session.getAttribute("stuId");
        if (stuId == null) {
            return R.error(403, "无法访问权限");
        }
        return R.data(courseService.cancelClick(stuId, Long.valueOf(couId)));
    }

    @GetMapping("/seeMore")
    @ApiOperation(value = "查看更多(多条件查询大小类、收藏量、点击量)")
    public R seeMore(@RequestParam("current") Long current,
                     @RequestParam(value = "maxTypeId", required = false, defaultValue = "") String maxTypeId,
                     @RequestParam(value = "minTypeId", required = false, defaultValue = "") String minTypeId,
                     @RequestParam(value = "couPrice", required = false, defaultValue = "0") int couPrice,
                     @RequestParam(value = "clickNum", required = false, defaultValue = "0") int clickNum
    ) {
        IPage<CouVO> page = new Page<>(current, 10);
        Long maxTId = 0L;
        Long minTId = 0L;
        if (!maxTypeId.equals("")) {
            maxTId = Long.valueOf(maxTypeId);
        }
        if (!minTypeId.equals("")) {
            minTId = Long.valueOf(minTypeId);
        }
        return R.data(courseService.seeMore(page, maxTId, minTId, couPrice, clickNum));
    }

    @GetMapping("/courseAppraise")
    @ApiOperation(value = "获取所有课程评价")
    public R courseAppraise(@RequestParam("couId") String couId,
                            @RequestParam("current") Long current) {
        IPage<CouAppraiseVO> page = new Page<>(current, 10);
        return R.data(courseService.courseAppraise(Long.valueOf(couId), page));
    }

    @GetMapping("/getCouAppraiseVO")
    @ApiOperation(value = "获取课程评价")
    public R getCouAppraiseVO(@RequestParam("couId") String couId,
                              @ApiIgnore HttpSession session) {
        Long stuId = (Long) session.getAttribute("stuId");
        if (stuId == null) {
            return R.error(403, "无访问权限");
        }
        return R.data(courseService.getCouAppraiseVO(Long.valueOf(couId), stuId));
    }

    @PostMapping("/addCouAppraise")
    @ApiOperation(value = "新增课程评价")
    public R addCouAppraise(@RequestBody CouAppraiseDTO couAppraiseDTO) {
        courseService.addCouAppraise(couAppraiseDTO);
        return courseService.addCouAppraise(couAppraiseDTO);
    }

    @GetMapping("/getCouEvSc")
    @ApiOperation(value = "获取课程评价分数")
    public Float getCouEvSc(@RequestParam("couId") Long couId) {
        return courseService.getCouEvSc(couId);
    }

    //******************************************************************************************************************************
    @GetMapping("/updatePurNum")
    @ApiOperation(value = "购买课程修改课程购买量")
    void updatePurNum(@RequestParam("couId") Long couId, @RequestParam("purchaseNum") Long purchaseNum) {
        courseService.updatePurNum(couId, purchaseNum);
    }

    @GetMapping("/getColletion")
    @ApiOperation(value = "获取课程收藏量")
    Long getColletion(@RequestParam("couId") Long couId) {
        return courseService.getColletion(couId);
    }

    @GetMapping("/selectCourseName")
    @ApiOperation(value = "查询课程名称")
    public List<String> selectCourseName(@RequestBody List<Long> couIds) {
        return courseService.selectCourseName(couIds);
    }

    @GetMapping("/getAllAnyFieryCourse")
    @ApiOperation(value = "查询所有各层面热度课程")
    public R getAllAnyFieryCourse() {
        return courseService.getAllAnyFieryCourse();
    }

    @GetMapping("/getParentTypeId")
    @ApiOperation(value = "获取所有父类别")
    public R getParentTypeId() {
        return R.data(courseService.getParentTypeId());
    }

    @GetMapping("/getCouTypeByParentTypeId")
    @ApiOperation(value = "根据父类别ID查询子类别")
    public R getCouTypeByParentTypeId(@RequestParam("parentTypeId") Long parentTypeId) {
        return R.data(courseService.getCouTypeByParentTypeId(parentTypeId));
    }

    @GetMapping("/getFieryCouTypeId")
    @ApiOperation(value = "获取所有火热子类别")
    public R getFieryCouTypeId() {
        return R.data(courseService.getFieryCouTypeId());
    }

    @GetMapping("/getCourseByInfo")
    @ApiOperation(value = "根据课程ID或者课程详情查询课程")
    public R getCourseByInfo(@RequestParam("courseInfo") String courseInfo) {
        return R.data(courseService.getCourseByInfo(courseInfo));
    }
}
