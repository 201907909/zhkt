package com.wisdom.stclass.controller;

import com.wisdom.base.util.R;
import com.wisdom.base.util.SnowflakeIdWorker;
import com.wisdom.stclass.bean.dto.*;
import com.wisdom.stclass.service.ClassService;
import com.wisdom.stclass.service.impl.QiniuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 班级业务模块
 * @author WisdomCR
 * @since  2022/10/30
 */
@Api(tags = "班级接口")
@RestController
@RequestMapping("/class")
public class ClassController {

    @Resource
    private ClassService classService;

    @Resource
    private QiniuService qiniuService;

//    @SneakyThrows
//    @PostMapping("/classFile")
//    @ApiOperation(value = "上传图片")
//    public R classFile(@RequestParam(value = "multipartFile") MultipartFile multipartFile, @ApiIgnore HttpSession session){
//        Long teaId = (Long) session.getAttribute("teaId");
//        if (teaId == null){
//            return R.error(403,"无访问权限");
//        }
//        return classService.classFile(multipartFile);
//    }

    @SneakyThrows
    @PostMapping("/classFile")
    @ApiOperation(value = "上传图片")
    public R classFile(@RequestParam(value = "multipartFile") MultipartFile multipartFile, @ApiIgnore HttpSession session){
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

    @PostMapping("/fileDelete")
    @ApiOperation("文件删除")
    public void deleteFile(@RequestParam("imgpath") String imgpath){
        if(imgpath!=null) {
            String[] path = imgpath.split("/");
            String key = path[3];
            try {
                qiniuService.deleteFile(key);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @PostMapping("/addClass")
    @ApiOperation(value="新增班级")
    public R addClass(@Validated @RequestBody ClassAddDTO classAddDTO, @ApiIgnore HttpSession session) {
        Long teaId = (Long) session.getAttribute("teaId");
        if (teaId == null){
            return R.error(403,"无访问权限");
        }
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        classAddDTO.setClassId(idWorker.nextId())
                .setTeaId(teaId);
        return classService.addClass(classAddDTO);
    }

    @GetMapping("/deleteClassById")
    @ApiOperation(value="删除班级")
    public R deleteClassById(@RequestParam(value = "id") Long id, @ApiIgnore HttpSession session) {
        Long teaId = (Long) session.getAttribute("teaId");
        if (teaId == null){
            return R.error(403,"无访问权限");
        }
        if (classService.deleteClassById(id)){
            return R.ok("删除班级成功");
        }else {
            return R.error("删除班级失败");
        }
    }

    @PostMapping("/updateClassById")
    @ApiOperation(value="修改班级信息")
    public R updateClassById(@Validated @RequestBody ClassUpdateDTO classUpdateDTO, @ApiIgnore HttpSession session) {
        Long teaId = (Long) session.getAttribute("teaId");
        if (teaId == null){
            return R.error(403,"无访问权限");
        }
        return classService.updateClassById(classUpdateDTO);
    }

    @GetMapping("/queryClassByTeaId")
    @ApiOperation(value="查询所有班级")
    public R queryClassByTeaId(@ApiIgnore HttpSession session) {
        Long teaId = (Long) session.getAttribute("teaId");
        if (teaId == null){
            return R.error(403,"无访问权限");
        }
        return R.data(classService.queryClassByTeaId(teaId));
    }

    @GetMapping("/queryClassByClaId")
    @ApiOperation(value="查询班级详细信息")
    public R queryClassByClaId(@RequestParam(value = "id") Long id) {
        return R.data(classService.queryClassByClaId(id));
    }

    // 查询班级课程信息，远程调用课程接口selectAllCourse、selectCourseByClassId进行统计分装
    @PostMapping("/queryClassCourseByClaId")
    @ApiOperation(value="查询班级课程信息")
    public R queryClassCourseByClaId(@Validated @RequestBody ClassCourseDTO classCourseDTO, @ApiIgnore HttpSession session) {
        Long teaId = (Long) session.getAttribute("teaId");
        if (teaId == null){
            return R.error(403,"无访问权限");
        }
        return R.data(classService.queryClassCourseByClaId(classCourseDTO));
    }

    @PostMapping("/classAddStuList")
    @ApiOperation(value="班级批量新增学生")
    public R classAddStuList(@Validated @RequestBody ClassAddDelStuSDTO classAddDleStuSDTO, @ApiIgnore HttpSession session) {
        Long teaId = (Long) session.getAttribute("teaId");
        if (teaId == null){
            return R.error(403,"无访问权限");
        }
        return classService.classAddStuList(classAddDleStuSDTO);
    }

    // 需要改一下，该班级已有的课程不能再添加
    @PostMapping("/classAddCouList")
    @ApiOperation(value="班级批量新增课程")
    public R classAddCouList(@Validated @RequestBody ClassAddDelCouSDTO classAddDelCouSDTO, @ApiIgnore HttpSession session) {
        Long teaId = (Long) session.getAttribute("teaId");
        if (teaId == null){
            return R.error(403,"无访问权限");
        }
        return classService.classAddCouList(classAddDelCouSDTO);
    }

    @PostMapping("/classDeletedStuList")
    @ApiOperation(value="班级批量删除学生")
    public R classDeletedStuList(@Validated @RequestBody ClassAddDelStuSDTO classAddDleStuSDTO, @ApiIgnore HttpSession session) {
        Long teaId = (Long) session.getAttribute("teaId");
        if (teaId == null){
            return R.error(403,"无访问权限");
        }
        Boolean isAdd = classService.classDeletedStuList(classAddDleStuSDTO);
        if (isAdd == true){
            return R.data("班级批量删除学生成功");
        }else {
            return R.error("班级批量删除学生失败");
        }
    }

    @PostMapping("/classDeletedCouList")
    @ApiOperation(value="班级批量删除课程")
    public R classDeletedCouList(@Validated @RequestBody ClassAddDelCouSDTO classAddDelCouSDTO, @ApiIgnore HttpSession session) {
        Long teaId = (Long) session.getAttribute("teaId");
        if (teaId == null){
            return R.error(403,"无访问权限");
        }
        Boolean isAdd = classService.classDeletedCouList(classAddDelCouSDTO);
        if (isAdd == true){
            return R.data("班级批量删除课程成功");
        }else {
            return R.error("班级批量删除课程失败");
        }
    }

    @GetMapping("/getStuDailyTime")
    @ApiOperation(value="获取学生每日学习时长")
    public Map<String,Object> getStuDailyTime(@RequestParam(value = "stuId") Long stuId) {
        return classService.getStuDailyTime(stuId);
    }

    @GetMapping("/getStuClockDate")
    @ApiOperation(value="获取学生打卡记录")
    public List<String> getStuClockDate(@RequestParam(value = "stuId") Long stuId) {
        return classService.getStuClockDate(stuId);
    }

    @GetMapping("/getStuContinuedClock")
    @ApiOperation(value="获取学生连续打卡天数")
    public Integer getStuContinuedClock(@RequestParam(value = "stuId") Long stuId) {
        return classService.getStuContinuedClock(stuId);
    }

}
