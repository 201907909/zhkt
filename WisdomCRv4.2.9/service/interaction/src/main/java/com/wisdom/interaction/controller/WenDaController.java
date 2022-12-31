package com.wisdom.interaction.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisdom.base.util.R;
import com.wisdom.base.util.Sample;
import com.wisdom.base.util.SampleAip;
import com.wisdom.information.bean.vo.CommentVO;
import com.wisdom.interaction.service.WenDaService;
import com.wisdom.interaction.service.impl.QiniuService;
import com.wisdom.interlocution.bean.dto.InterlocutionDTO;
import com.wisdom.interlocution.bean.dto.ReplyDTO;
import com.wisdom.interlocution.bean.vo.IssueVO;
import com.wisdom.interlocution.bean.vo.ReplyVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/wenda")
@Api(tags = "问答管理接口")
@CrossOrigin
@Slf4j
public class WenDaController {

    @Autowired
    private WenDaService wenDaService;

    @Autowired
    private QiniuService qiniuService;

    /**
     * 发布问题
     */
    @ApiOperation(value = "发布问题")
    @PostMapping("/add")
    public R add(@RequestBody InterlocutionDTO interlocutionDTO) {
        Long userId = interlocutionDTO.getUserId();
        if (userId == null) {
            return R.error(403, "无访问权限");
        }
        return R.ok(wenDaService.add(interlocutionDTO));
    }

    /**
     *查询所有类别
     */
    @ApiOperation(value = "查询所有类别")
    @GetMapping("/getAllType")
    public R getAllType() {
        return R.data(wenDaService.getAllType());
    }

    /**
     * 回答问题
     */
    @ApiOperation(value = "回答问题")
    @PostMapping("/reply")
    public R reply(@RequestBody ReplyDTO replyDTO) {
        Long userId = replyDTO.getUserId();
        if (userId == null) {
            return R.error(403, "无访问权限");
        }
        return R.ok(wenDaService.reply(replyDTO));
    }

    @ApiOperation(value = "学生浏览所有的问答")
    @GetMapping("/getAllWenDa")
    public R getAllWenDa(@RequestParam("current") Long current) {
        IPage<IssueVO> page = new Page<>(current, 10);
        return R.data(wenDaService.getAllWenDa(page));
    }

    @ApiOperation(value = "所有问题的条数")
    @GetMapping("/getAllTotal")
    public R getAllTotal(){
        return R.data(wenDaService.getAllTotal());
    }

    @ApiOperation(value = "教师浏览所有的问答")
    @GetMapping("/teaGetAllWenDa")
    public R teaGetAllWenDa(@RequestParam("current") Long current,
                            @ApiIgnore HttpSession session) {
        Long teaId = (Long) session.getAttribute("teaId");
        if (teaId == null) {
            return R.error(403, "无访问权限");
        }
        IPage<IssueVO> page = new Page<>(current, 7);
        return R.data(wenDaService.teaGetAllWenDa(page, teaId));
    }

    @ApiOperation(value = "查看问题详情")
    @GetMapping("/issueVODetail")
    public R issueVODetail(@RequestParam("queId") String queId,
                           @ApiIgnore HttpSession session) {

        Long stuId = (Long) session.getAttribute("stuId");
        Long teaId = (Long) session.getAttribute("teaId");
        Long userId = 0L;
        if (stuId != null) {
            userId = stuId;
        }else {
            userId = teaId;
        }
        return R.data(wenDaService.issueVODetail(Long.valueOf(queId), userId));
    }

    @ApiOperation(value = "查看问题的所有回答")
    @GetMapping("/replyVOSDetail")
    public R replyVOSDetail(@RequestParam("queId") String queId,
                           @RequestParam("current") Long current) {
        IPage<ReplyVO> page = new Page<>(current, 10);
        return R.data(wenDaService.replyVOSDetail(Long.valueOf(queId), page));
    }

    @ApiOperation(value = "教师查看问题详情")
    @GetMapping("/teaIssueVODetail")
    public R teaIssueVODetail(@RequestParam("queId") String queId,
                           @ApiIgnore HttpSession session) {
        Long stuId = (Long) session.getAttribute("stuId");
        Long teaId = (Long) session.getAttribute("teaId");
        Long userId = 0L;
        if (stuId != null) {
            userId = stuId;
        }else {
            userId = teaId;
        }
        return R.data(wenDaService.teaIssueVODetail(Long.valueOf(queId), userId));
    }

    @ApiOperation(value = "点赞")
    @PostMapping("/click")
    public R click(@RequestParam(value = "queId") String queId,
                   @ApiIgnore HttpSession session) {
        Long stuId = (Long) session.getAttribute("stuId");
        Long teaId = (Long) session.getAttribute("teaId");
        Long userId = 0L;
        if (stuId != null) {
            userId = stuId;
        }else if(teaId != null){
            userId = teaId;
        }else {
            return R.error(403, "无访问权限");
        }
        return R.data(wenDaService.click(Long.valueOf(queId), userId));
    }

    @ApiOperation(value = "取消点赞")
    @PostMapping("/cancelClick")
    public R cancelClick(@RequestParam(value = "queId") String queId,
                         @ApiIgnore HttpSession session) {
        Long stuId = (Long) session.getAttribute("stuId");
        Long teaId = (Long) session.getAttribute("teaId");
        Long userId = 0L;
        if (stuId != null) {
            userId = stuId;
        }else if(teaId != null){
            userId = teaId;
        }else {
            return R.error(403, "无访问权限");
        }
        return R.data(wenDaService.cancelClick(Long.valueOf(queId), userId));
    }

    @ApiOperation(value = "采纳回答")
    @PostMapping("/adopt")
    public R adopt(@RequestParam(value = "replyId") String replyId,
                   @RequestParam(value = "queId") String queId,
                   @ApiIgnore HttpSession session) {
        Long stuId = (Long) session.getAttribute("stuId");
        Long teaId = (Long) session.getAttribute("teaId");
        Long userId = 0L;
        if (stuId != null) {
            userId = stuId;
        }else if(teaId != null){
            userId = teaId;
        }else {
            return R.error(403, "无访问权限");
        }
        wenDaService.adopt(Long.valueOf(replyId), userId, Long.valueOf(queId));
        return R.ok();
    }

    @ApiOperation(value = "取消采纳")
    @PostMapping("/cancelAdopt")
    public R cancelAdopt(@RequestParam(value = "replyId") String replyId,
                   @ApiIgnore HttpSession session) {
        Long stuId = (Long) session.getAttribute("stuId");
        Long teaId = (Long) session.getAttribute("teaId");
        Long userId = 0L;
        if (stuId != null) {
            userId = stuId;
        }else if(teaId != null){
            userId = teaId;
        }else {
            return R.error(403, "无访问权限");
        }
        wenDaService.cancelAdopt(Long.valueOf(replyId), userId);
        return R.ok();
    }

    @ApiOperation(value = "搜索问题")
    @GetMapping("/search")
    public R search(@RequestParam("val") String val, @RequestParam("current") Long current ) {
        IPage<IssueVO> page = new Page<>(current, 10);
        return R.data(wenDaService.search(val, page));
    }

    @SneakyThrows
    @PostMapping("/wenDaFile")
    @ApiOperation(value = "上传图片")
    public String wenDaFile(@RequestParam(value = "file") MultipartFile file, @ApiIgnore HttpSession session){
        Long sessionId = (Long) session.getAttribute("sessionId");
        if (sessionId == null){
            return "无访问权限";
        }
        if(file.isEmpty()) {
            return "error";
        }

        try {
            String fileUrl=qiniuService.saveImage(file);
            return fileUrl;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "fail";
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

    @PostMapping("/deletedWenDa")
    @ApiOperation(value = "删除")
    public R deletedWenDa(@RequestBody List<String> queId) {
        List<Long> qId = new ArrayList<>();
        for (int i=0;i<queId.size();i++) {
            qId.add(Long.valueOf(queId.get(i)));
        }
        wenDaService.deletedWenDa(qId);
        return R.ok("删除成功");
    }

    @ApiOperation(value = "修改问题逻辑删除")
    @PostMapping("/remoteUpdateWenDa")
    void remoteUpdateWenDa(@RequestBody InterlocutionDTO interlocutionDTO) {
        wenDaService.remoteUpdateWenDa(interlocutionDTO);
    }

    @GetMapping("/stuNoticeWenDa")
    List<IssueVO> stuNoticeWenDa(@RequestParam("stuId") Long stuId) {
        return wenDaService.stuNoticeWenDa(stuId);
    }

    //------------------------------------------------------------------------------------------------------------ 以下是我的消息
    @GetMapping("/getMyAllReply")
    @ApiOperation(value="我的所有回答")
    public R getMyAllReplys(@ApiIgnore HttpSession session){
        Long stuId = (Long) session.getAttribute("stuId");
        if (stuId == null) {
            return R.error(403, "无访问权限");
        }
        return R.data(wenDaService.getMyAllReplys(stuId));
    }

    @GetMapping("/getMyReplyByqueId")
    @ApiOperation(value="查看我对问题的回答")
    public R getMyReplyByqueId(@RequestParam("queId") String queId, @ApiIgnore HttpSession session){
        Long stuId = (Long) session.getAttribute("stuId");
        if (stuId == null) {
            return R.error(403, "无访问权限");
        }
        return R.data(wenDaService.getMyReplyByqueId(Long.valueOf(queId), stuId));
    }

    @GetMapping("/getMyIssues")
    @ApiOperation(value="我的所有提问")
    public R getMyIssues(@ApiIgnore HttpSession session){
        Long stuId = (Long) session.getAttribute("stuId");
        if (stuId == null) {
            return R.error(403, "无访问权限");
        }
        return R.data(wenDaService.getMyIssues(stuId));
    }

    @GetMapping("/getReplyMyIssue")
    @ApiOperation(value="回答我的提问")
    public R getReplyMyIssue(@RequestParam("queId") String queId) {
        return R.data(wenDaService.getReplyMyIssue(Long.valueOf(queId)));
    }

}
