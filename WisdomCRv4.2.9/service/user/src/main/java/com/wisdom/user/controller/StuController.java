package com.wisdom.user.controller;

import com.wisdom.base.util.IpUtil;
import com.wisdom.base.util.R;
import com.wisdom.user.bean.dto.UserLoginDTO;
import com.wisdom.user.bean.dto.StuUpdateDTO;
import com.wisdom.user.bean.vo.*;
import com.wisdom.user.service.StudentService;
import com.wisdom.user.service.impl.QiniuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@RestController
@RequestMapping("/student")
@Api(tags = "学生接口")
@CrossOrigin
public class StuController {

    @Resource
    private StudentService studentService;
    @Resource
    private MailController mailController;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private QiniuService qiniuService;

    @PostMapping("/getPasswordCode")
    @ApiOperation(value="忘记密码发送邮箱")
    public R getPasswordCode(@RequestParam(value = "to") String to, @ApiIgnore HttpSession session) {
        if(!studentService.isExist(to)){
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
    public R login(@Validated @RequestBody UserLoginDTO userLoginDTO, @ApiIgnore HttpSession session, @ApiIgnore HttpServletRequest request){
        LoginVO loginVO = studentService.login(userLoginDTO);
        if(loginVO != null) {
            session.setAttribute("sessionId", loginVO.getId());
            session.setAttribute("stuId", loginVO.getUserId());
            //学生id做key，访问者id做value
            stringRedisTemplate.opsForValue().set(Long.toString(loginVO.getUserId()), IpUtil.getIpAddress(request));
            return R.data(Long.toString(loginVO.getUserId()), "登录成功");
        }else {
            return R.error("登录失败");
        }
    }

    @GetMapping("/getStuDetails")
    @ApiOperation(value="获取学生详细信息")
    public R getStuDetails(@ApiIgnore HttpSession httpSession){
        Long sessionId = (Long) httpSession.getAttribute("sessionId");
        if (sessionId == null){
            return R.error(403,"无访问权限");
        }
        return R.data(studentService.getStuDetails(sessionId));
    }

//    @SneakyThrows
//    @PostMapping("/stuFile")
//    @ApiOperation(value = "上传图片")
//    public String stuFile(@RequestParam(value = "multipartFile") MultipartFile multipartFile, @ApiIgnore HttpSession session){
//        Long sessionId = (Long) session.getAttribute("sessionId");
//        if (sessionId == null){
//            return "无访问权限";
//        }
//        return studentService.stuFile(multipartFile);
//    }

    @SneakyThrows
    @PostMapping("/stuFile")
    @ApiOperation(value = "上传图片")
    public R stuFile(@RequestParam(value = "multipartFile") MultipartFile multipartFile, @ApiIgnore HttpSession session){
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
    @ApiOperation(value="更新学生信息")
    private R updateInfo(@Validated @RequestBody StuUpdateDTO stuUpdateDTO, @ApiIgnore HttpSession httpSession){
        Long sessionId = (Long) httpSession.getAttribute("sessionId");
        if (sessionId == null){
            return R.error(403,"无修改权限");
        }
        stuUpdateDTO.setId(sessionId);
        return studentService.updateInfo(stuUpdateDTO);
    }

    @PostMapping("/updatePassword")
    @ApiOperation(value="更新学生密码")
    private R updatePassword(@RequestParam(value = "password") String password, @ApiIgnore HttpSession httpSession){
        Long sessionId = (Long) httpSession.getAttribute("sessionId");
        if (sessionId == null){
            return R.error(403,"无访问权限");
        }
        if (studentService.updatePassword(password, sessionId)){
            return R.ok("修改密码成功");
        }else {
            return R.error("修改密码失败");
        }
    }

    @PostMapping("/forgetPassword")
    @ApiOperation(value="忘记密码")
    private R forgetPassword(@RequestParam(value = "password") String password, @ApiIgnore HttpSession session){
        Object verificationMailP = session.getAttribute("verificationMailP");
        if(verificationMailP==null){
            return R.error();
        }
        if (studentService.forgetPassword((String) verificationMailP,password)){
            session.removeAttribute("verificationMailP");
            return R.ok("修改密码成功");
        }else {
            return R.error("修改密码失败");
        }
    }

    @GetMapping("/queryStudentByClassId")
    @ApiOperation(value = "根据班级ID查询所有学生")
    private List<StudentDetailedVO> queryStudentByClassId(Long classId) {
        return studentService.queryStudentByClassId(classId);
    }

    @GetMapping("/queryStudentProgressByStuId")
    @ApiOperation(value = "根据学生ID查询学生学习时长")
    private StudentProgressVO queryStudentProgressByStuId(Long stuId) {
        return studentService.queryStudentProgressByStuId(stuId);
    }

    @PostMapping("/updateStuStudyTime")
    @ApiOperation(value="更新学生学习时长")
    private R updateStuStudyTime(@RequestParam(value = "stuStudyTime") Long stuStudyTime, @ApiIgnore HttpSession httpSession){
        Long sessionId = (Long) httpSession.getAttribute("sessionId");
        if (sessionId == null){
            return R.error(403,"无访问权限");
        }
        if (studentService.updateStuStudyTime(stuStudyTime, sessionId)){
            return R.ok("修改学习时长成功");
        }else {
            return R.error("修改学习时长失败");
        }
    }

    @PostMapping("/classQueryStuScore")
    @ApiOperation(value="班级查询学生积分")
    private List<ScoreClassVO> classQueryStuScore(@RequestParam(value = "stuId") Long stuId){
        return studentService.classQueryStuScore(stuId);
    }

    @PostMapping("/addClock")
    @ApiOperation(value="更新打卡记录")
    private R addClock(@RequestParam(value = "stuId") Long stuId, @ApiIgnore HttpSession httpSession){
        Long sessionId = (Long) httpSession.getAttribute("sessionId");
        if (sessionId == null){
            return R.error(403,"无访问权限");
        }
        if (studentService.addClock(stuId)){
            return R.ok("修改打卡记录成功");
        }else {
            return R.error("修改打卡记录失败");
        }
    }

    @GetMapping("/classQueryAllStu")
    @ApiOperation(value="查看所有学生")
    public R classQueryAllStu(@RequestParam(value = "email") String email, @ApiIgnore HttpSession session) {
        Long sessionId = (Long) session.getAttribute("sessionId");
        if (sessionId == null){
            return R.error(403,"无访问权限");
        }
        List<StuAboutClassVO> stuAboutClassVOS = studentService.classQueryAllStu(email);
        if (stuAboutClassVOS != null){
            return R.data(stuAboutClassVOS, "查询所有学生成功");
        }else {
            return R.error("查询所有学生成功");
        }
    }

    @GetMapping("/getStuByStuId")
    StudentDetailedVO getStuByStuId(@RequestParam("stuId") Long stuId) {
        return studentService.getStuByStuId(stuId);
    }

    @GetMapping("/selectStudentName")
    @ApiOperation(value = "查询学生名称")
    public List<String> selectStudentName(@RequestBody List<Long> stuIds) {
        return studentService.selectStudentName(stuIds);
    }

    @GetMapping("/getStuScoreByStuId")
    @ApiOperation(value = "获取学生积分")
    public int getStuScoreByStuId(@RequestParam("stuId") Long stuId) {
        return studentService.getStuScoreByStuId(stuId);
    }

    @PostMapping("/updateScore")
    @ApiOperation(value = "修改学生积分")
    public void updateScore(@RequestParam("stuId") Long stuId, @RequestParam("stuScore") int stuScore) {
        studentService.updateScore(stuId, stuScore);
    }

}
