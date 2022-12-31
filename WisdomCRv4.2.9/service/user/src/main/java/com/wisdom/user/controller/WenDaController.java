package com.wisdom.user.controller;

import com.wisdom.base.util.R;
import com.wisdom.user.service.WenDaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/interlocution")
@Api(tags = "问答接口")
@CrossOrigin
public class WenDaController {

    @Autowired
    private WenDaService wenDaService;

    @GetMapping("/getMyAllReply")
    @ApiOperation(value="我的所有回答")
    public R getMyAllReplys(@ApiIgnore HttpSession session){
        Long stuId = (Long) session.getAttribute("stuId");
        return R.data(wenDaService.getMyAllReplys(stuId));
    }

    @GetMapping("/getMyReplyByqueId")
    @ApiOperation(value="查看我对问题的回答")
    public R getMyReplyByqueId(@RequestParam("queId") String queId, @ApiIgnore HttpSession session){
        Long stuId = (Long) session.getAttribute("stuId");
        return R.data(wenDaService.getMyReplyByqueId(Long.valueOf(queId), stuId));
    }

    @GetMapping("/getMyIssues")
    @ApiOperation(value="我的所有提问")
    public R getMyIssues(@ApiIgnore HttpSession session){
        Long stuId = (Long) session.getAttribute("stuId");
        return R.data(wenDaService.getMyIssues(stuId));
    }

    @GetMapping("/getReplyMyIssue")
    @ApiOperation(value="回答我的提问")
    public R getReplyMyIssue(@RequestParam("queId") String queId) {
        return R.data(wenDaService.getReplyMyIssue(Long.valueOf(queId)));
    }
}
