package com.wisdom.course.controller.practice;

import com.wisdom.base.util.R;
import com.wisdom.base.util.SnowflakeIdWorker;
import com.wisdom.course.service.TeacherPracticeService;
import com.wisdom.course.service.impl.QiniuService;
import com.wisdom.practice.bean.dto.PracticeAddUpDTO;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("teacherPractice")
public class TeacherPracticeController {
    @Resource
    private TeacherPracticeService teacherPracticeService;
    @Resource
    private QiniuService qiniuService;

//    @SneakyThrows
//    @PostMapping("/uploadPicture")
//    @ApiOperation(value = "上传图片")
//    public R uploadPicture(@RequestParam(value = "multipartFile") MultipartFile multipartFile, @ApiIgnore HttpSession session){
//        Long teaId = (Long) session.getAttribute("teaId");
//        if (teaId == null){
//            return R.error(403,"无访问权限");
//        }
//        return teacherPracticeService.uploadPicture(multipartFile);
//    }

//    @SneakyThrows
//    @PostMapping("/deletePicture")
//    @ApiOperation(value = "删除图片")
//    public R deletePicture(@RequestParam(value = "oldImg") String oldImg, @ApiIgnore HttpSession session){
//        Long teaId = (Long) session.getAttribute("teaId");
//        if (teaId == null){
//            return R.error(403,"无访问权限");
//        }
//        return teacherPracticeService.deletePicture(oldImg);
//    }

    @SneakyThrows
    @PostMapping("/uploadPicture")
    @ApiOperation(value = "上传图片")
    public R uploadPicture(@RequestParam(value = "multipartFile") MultipartFile multipartFile, @ApiIgnore HttpSession session){
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

    @PostMapping("/addPractice")
    @ApiOperation(value = "教师创建一个线下实践任务")
    public R addPractice(@Validated @RequestBody PracticeAddUpDTO practiceAddUpDTO, @ApiIgnore HttpSession httpSession) {
        Long teaId = (Long) httpSession.getAttribute("teaId");
        if (teaId == null) {
            return R.error(403, "无访问权限");
        }
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        practiceAddUpDTO.setTeaId(teaId)
                .setPraId(idWorker.nextId());
        if (teacherPracticeService.addPractice(practiceAddUpDTO)) {
            return R.ok("新建实践成功");
        }
        return R.error("新建实践失败");
    }

    @PostMapping("/deletePractice")
    @ApiOperation(value = "教师删除一个线下实践任务")
    public R deletePractice(@RequestParam(value = "id") Long id, @RequestParam(value = "teaId") Long teaId, @ApiIgnore HttpSession httpSession) {
        String sessionTeaId = (String) httpSession.getAttribute("teaId");
        if (sessionTeaId == null) {
            return R.error(403, "无访问权限");
        }else {
            if (!sessionTeaId.equals(Long.toString(teaId))){
                return R.error(403,"该实践不归您所有，无删除权限");
            }
        }
        if (teacherPracticeService.deletePractice(id, teaId)) {
            return R.ok("删除实践成功");
        }
        return R.error("删除实践失败");
    }

    @PostMapping("/editPractice")
    @ApiOperation(value = "教师修改线下实践详情")
    public R editPractice(@Validated @RequestBody PracticeAddUpDTO practiceAddUpDTO, @ApiIgnore HttpSession httpSession) {
        Long teaId = (Long) httpSession.getAttribute("teaId");
        if (teaId == null) {
            return R.error(403, "无访问权限");
        }else {
            if (!teaId.equals(practiceAddUpDTO.getTeaId())){
                return R.error(403,"该实践不归您所有，无修改权限");
            }
        }
        return teacherPracticeService.editPractice(practiceAddUpDTO);
    }

    @GetMapping("/getAllPracticeInfoByTea")
    @ApiOperation(value = "教师查看所有线下实践任务")
    public R getAllPracticeInfoByTea() {
        return R.data(teacherPracticeService.getAllPracticeInfoByTea());
    }

    @GetMapping("/getPracticeInfoByTea")
    @ApiOperation(value = "教师查看指定线下实践任务")
    public R getPracticeInfoByTea(@RequestParam Long id, @ApiIgnore HttpSession httpSession) {
        Long teaId = (Long) httpSession.getAttribute("teaId");
        if (teaId == null) {
            return R.error(403, "无访问权限");
        }
        return R.data(teacherPracticeService.getPracticeInfoByTea(id));
    }

    @GetMapping("/getAllPracticeType")
    @ApiOperation(value = "教师查看所有线下实践类别")
    public R getAllPracticeType() {
        return R.data(teacherPracticeService.getAllPracticeType());
    }

    @GetMapping("/getAllCourseByTeaId")
    @ApiOperation(value = "查询指定教师所有课程")
    public R getAllCourseByTeaId() {
        return R.data(teacherPracticeService.getAllCourseByTeaId(1037789643278618368L));
    }

    @GetMapping("/getPracticeByInfo")
    @ApiOperation(value = "根据实践信息查询指定范围内实践")
    public R getPracticeByInfo(@RequestParam String info) {
        return R.data(teacherPracticeService.getPracticeByInfo(info));
    }

    @GetMapping("/getOverduePractice")
    @ApiOperation(value = "查询已过期实践")
    public R getOverduePractice() {
        return R.data(teacherPracticeService.getOverduePractice());
    }
}
