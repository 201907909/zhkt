package com.wisdom.user.controller;

import com.wisdom.base.util.R;
import com.wisdom.user.bean.dto.GetClockDaysOfMonthDTO;
import com.wisdom.user.bean.vo.StuClockDetailsVO;
import com.wisdom.user.service.StudentClockService;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.ParseException;

/**
 * @author WisdomCR
 * @since 2022/11/7
 */
@RestController
@RequestMapping("/studentClock")
public class StudentClockController {
    @Resource
    private StudentClockService studentClockService;

    @GetMapping("/automaticClock")
    @ApiOperation(value="自动打卡")
    public String automaticClock(@ApiIgnore HttpSession httpSession) {
        Long stuId = (Long) httpSession.getAttribute("stuId");
        if (stuId == null) {
            return "无访问权限";
        }
        if (studentClockService.clock(stuId) != null) {
            return "打卡成功";
        }
        return "打卡失败";
    }

    @GetMapping("/clock")
    @ApiOperation(value="打卡")
    public R clock(@ApiIgnore HttpSession httpSession) {
        Long stuId = (Long) httpSession.getAttribute("stuId");
        if (stuId == null) {
            return R.error(403, "无访问权限");
        }
        return studentClockService.clock(stuId);
    }

    @PostMapping("/getSignInDaysOfTheMonth")
    @ApiOperation(value="获取某月签到情况")
    public R getSignInDaysOfTheMonth(@ApiIgnore HttpSession httpSession) throws ParseException {
        Long stuId = (Long) httpSession.getAttribute("stuId");
        if (stuId == null) {
            return R.error(403, "无访问权限");
        }
        GetClockDaysOfMonthDTO getClockDaysOfMonthDTO = new GetClockDaysOfMonthDTO();
        getClockDaysOfMonthDTO.setStuId(stuId);
        return R.data(studentClockService.getSignInDaysOfTheMonth(getClockDaysOfMonthDTO));
    }

    @PostMapping("/countersign")
    @ApiOperation(value="补签")
    public R countersign(@Validated @RequestBody GetClockDaysOfMonthDTO getClockDaysOfMonthDTO, @ApiIgnore HttpSession httpSession) throws ParseException {
        Long stuId = (Long) httpSession.getAttribute("stuId");
        if (stuId == null) {
            return R.error(403, "无访问权限");
        }
        getClockDaysOfMonthDTO.setStuId(stuId);
        if (studentClockService.countersign(getClockDaysOfMonthDTO)) {
            return R.ok();
        }
        return R.error("当前日期已签到");
    }
}
