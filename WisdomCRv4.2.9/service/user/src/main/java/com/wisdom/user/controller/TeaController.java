package com.wisdom.user.controller;

import com.wisdom.base.util.R;
import com.wisdom.user.bean.dto.TeaUpdateDTO;
import com.wisdom.user.bean.dto.UserLoginDTO;
import com.wisdom.user.bean.vo.LoginVO;
import com.wisdom.user.bean.vo.TeacherDetailedVO;
import com.wisdom.user.service.TeacherService;
import com.wisdom.user.service.impl.QiniuService;
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

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@RestController
@RequestMapping("/teacher")
@Api(tags = "教师接口")
@CrossOrigin
public class TeaController {

    @Resource
    private TeacherService teacherService;
    @Resource
    private MailController mailController;
    @Resource
    private QiniuService qiniuService;

    @PostMapping("/getPasswordCode")
    @ApiOperation(value="忘记密码发送邮箱")
    public R getPasswordCode(@RequestParam(value = "to") String to, @ApiIgnore HttpSession session) {
        if(!teacherService.isExist(to)){
            return R.error("用户不存在");
        }
        R registerCode = mailController.forgetPassword(to);
        if(!registerCode.get("code").toString().equals("200")){
            return R.error();
        }
        session.setAttribute("verificationPassword",registerCode.get("verification"));
        return R.ok("发送成功");
    }

    @PostMapping("/hasPassWordCode")
    @ApiOperation(value="忘记密码是否正确")
    public R hasPassWordCode(@RequestParam(value = "to") String to, @RequestParam(value = "code") String code, @ApiIgnore HttpSession session){
        Object verificationPassword = session.getAttribute("verificationPassword");
        if(code==null){
            return R.error("验证码为空");
        }
        if(!verificationPassword.equals(code)){
            return R.error("验证码错误");
        }
        session.removeAttribute("verificationPassword");
        session.setAttribute("verificationMailP",to);
        return R.ok();
    }

    @GetMapping("/logout")
    @ApiOperation(value="用户登出")
    public R logout(@ApiIgnore HttpSession session){
        session.invalidate();
        return R.ok();
    }

    @PostMapping("/login")
    @ApiOperation(value="用户登录")
    public R login(@Validated @RequestBody UserLoginDTO userLoginDTO, @ApiIgnore HttpSession session){
        LoginVO loginVO = teacherService.login(userLoginDTO);
        if(loginVO != null) {
            session.setAttribute("sessionId", loginVO.getId());
            session.setAttribute("teaId", loginVO.getUserId());
            return R.data(Long.toString(loginVO.getUserId()), "登录成功");
        }else {
            return R.error("登录失败");
        }
    }

    @GetMapping("/getTeaDetails")
    @ApiOperation(value="获取教师详细信息")
    public R getTeaDetails(@ApiIgnore HttpSession httpSession){
        Long sessionId = (Long) httpSession.getAttribute("sessionId");
        if (sessionId == null){
            return R.error(403,"无访问权限");
        }
        return R.data(teacherService.getTeaDetails(sessionId));
    }

//    @SneakyThrows
//    @PostMapping("/teaFile")
//    @ApiOperation(value = "上传图片")
//    public R teaFile(@RequestParam(value = "multipartFile") MultipartFile multipartFile, @ApiIgnore HttpSession session){
//        Long sessionId = (Long) session.getAttribute("sessionId");
//        if (sessionId == null){
//            return R.error(403,"无访问权限");
//        }
//        return teacherService.teaFile(multipartFile);
//    }

    @SneakyThrows
    @PostMapping("/teaFile")
    @ApiOperation(value = "上传图片")
    public R teaFile(@RequestParam(value = "multipartFile") MultipartFile multipartFile, @ApiIgnore HttpSession session){
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
    public void deleteFile(String imgpath,String filepath){
        if(imgpath!=null) {
            String[] path = imgpath.split("/");
            String key = path[3];
            try {
                qiniuService.deleteFile(key);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(filepath!=null){
            String[] path = filepath.split("/");
            String key = path[3];
            try {
                qiniuService.deleteFile(key);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @PostMapping("/updateInfo")
    @ApiOperation(value="更新教师信息")
    private R updateInfo(@Validated @RequestBody TeaUpdateDTO teaUpdateDTO, @ApiIgnore HttpSession httpSession){
        Long sessionId = (Long) httpSession.getAttribute("sessionId");
        if (sessionId == null){
            return R.error(403,"无修改权限");
        }
        teaUpdateDTO.setId(sessionId);
        return teacherService.updateInfo(teaUpdateDTO);
    }

    @PostMapping("/updatePassword")
    @ApiOperation(value="更新教师密码")
    private R updatePassword(@RequestParam(value = "password") String password, @ApiIgnore HttpSession httpSession){
        Long sessionId = (Long) httpSession.getAttribute("sessionId");
        if (sessionId == null){
            return R.error(403,"无访问权限");
        }
        if (teacherService.updatePassword(password, sessionId)){
            return R.ok("修改密码成功");
        }else {
            return R.error("修改密码失败");
        }
    }

    @PostMapping("/forgetPassword")
    @ApiOperation(value="忘记密码")
    private R forgetPassword(@RequestParam(value = "password") String password, HttpSession session){
        Object verificationMailP = session.getAttribute("verificationMailP");
        if(verificationMailP==null){
            return R.error();
        }
        if (teacherService.forgetPassword((String) verificationMailP,password)){
            session.removeAttribute("verificationMailP");
            return R.ok("修改密码成功");
        }else {
            return R.error("修改密码失败");
        }
    }

    @GetMapping("/updateCourseNum")
    @ApiOperation(value="更新课程数")
    private Boolean updateCourseNum(@ApiIgnore HttpSession httpSession){
        Long sessionId = (Long) httpSession.getAttribute("sessionId");
        return teacherService.updateCourseNum(sessionId);
    }

    @GetMapping("/getTeaNameByTeaId")
    @ApiOperation(value = "通过teaId返回教师名字")
    public String getTeaNameByTeaId(@RequestParam("teaId") Long teaId) {
        return teacherService.getTeaDetailsP(teaId);
    }

    @GetMapping("/getTeaByTeaId")
    TeacherDetailedVO getTeaByTeaId(@RequestParam("teaId") Long teaId) {
        return teacherService.getTeaByTeaId(teaId);
    }

}
