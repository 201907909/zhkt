package com.wisdom.user.controller;


import com.wisdom.base.util.R;
import com.wisdom.user.service.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/notice")
@Api(tags = "系统通知接口")
@CrossOrigin
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @ApiOperation(value = "学生系统通知")
    @GetMapping("/stuNotice")
    public R stuNotice(@ApiIgnore HttpSession session) {
        Long stuId = (Long) session.getAttribute("stuId");
        if (stuId == null) {
            return R.error(403, "无访问权限");
        }
        return R.data(noticeService.stuNotice(stuId));
    }

    @ApiOperation(value = "教师系统通知")
    @GetMapping("/teaNotice")
    public R teaNotice(@ApiIgnore HttpSession session) {
        Long teaId = (Long) session.getAttribute("teaId");
        if (teaId == null) {
            return R.error(403, "无访问权限");
        }
        return R.data(noticeService.teaNotice(teaId));
    }

    @ApiOperation(value = "删除通知")
    @PostMapping("/deleted")
    public R deleted(@RequestParam("noticeId") List<String> noticeId) {
        List<Long> nId = new ArrayList<>();
        for (int i = 0; i < noticeId.size(); i++) {
            nId.add(Long.valueOf(noticeId.get(i)));
        }
        noticeService.deleted(nId);
        return R.ok("删除成功");
    }

    @ApiOperation(value = "申请复审")
    @GetMapping("/application")
    public R application(@RequestParam("noticeId") String noticeId) {
        noticeService.application(Long.valueOf(noticeId));
        return R.ok();
    }

    @PostMapping("/addNotice")
    void addNotice(@RequestParam("userId") Long userId, @RequestParam("noticeId") Long noticeId) {
        noticeService.addNotice(userId, noticeId);
    }
}
