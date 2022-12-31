package com.wisdom.interaction.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisdom.base.util.R;
import com.wisdom.base.util.Sample;
import com.wisdom.base.util.SampleAip;
import com.wisdom.information.bean.dto.InforDTO;
import com.wisdom.information.bean.vo.CommentVO;
import com.wisdom.information.bean.vo.InforVO;
import com.wisdom.interaction.service.InforService;
import com.wisdom.interaction.service.impl.QiniuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/information")
@Api(tags = "资讯管理接口")
@CrossOrigin
public class InformationController {

    @Autowired
    private InforService inforService;

    @Autowired
    private QiniuService qiniuService;

    @Value("${APP_ID}")
    public String APP_ID;
    @Value("${API_KEY}")
    public String API_KEY;
    @Value("${SECRET_KEY}")
    public String SECRET_KEY;

    @ApiOperation(value = "浏览所有资讯")
    @GetMapping("/getAllInfor")
    public R getAllInfor(@RequestParam("current") Long current,
                         @ApiIgnore HttpSession session) {
        Long stuId = (Long) session.getAttribute("stuId");
        Long teaId = (Long) session.getAttribute("teaId");
        Long userId = 0L;
        if (stuId != null) {
            userId = stuId;
        }else {
            userId = teaId;
        }
        IPage<InforVO> page = new Page<>(current, 10);
        return R.data(inforService.getAllInfor(page, userId));
    }

    @ApiOperation(value = "所有资讯的条数")
    @GetMapping("/getAllTotal")
    public R getAllTotal(){
        return R.data(inforService.getAllTotal());
    }

    @ApiOperation(value = "教师浏览资讯")
    @GetMapping("/getAllInforByTeaId")
    public R getAllInforByTeaId(@RequestParam("current") Long current, @ApiIgnore HttpSession session) {
        Long teaId = (Long)session.getAttribute("teaId");
        if(teaId == null) {
            return R.error(403, "无访问权限");
        }
        IPage<InforVO> page = new Page<>(current, 7);
        return R.data(inforService.getAllInforByTeaId(page, teaId));
    }

    @PostMapping("/add")
    @ApiOperation(value = "发布资讯")
    public R add(@RequestBody InforDTO inforDTO) {
        Long teaId = inforDTO.getTeaId();
        if (teaId == null) {
            return R.error(403, "无访问权限");
        }
        return inforService.add(inforDTO);
    }

    @ApiOperation(value = "查询资讯详情")
    @GetMapping("/DetailInfoByInfoId")
    public R infoDetailByInfoId(@RequestParam("infoId") String infoId,
                                @ApiIgnore HttpSession session) {
        Long stuId = (Long) session.getAttribute("stuId");
        Long teaId = (Long) session.getAttribute("teaId");
        Long userId = 0L;
        if (stuId != null) {
            userId = stuId;
        }else {
            userId = teaId;
        }
        return R.data(inforService.infoDetailByInfoId(Long.valueOf(infoId), userId));
    }

    @ApiOperation(value = "查询资讯评论")
    @GetMapping("/commentByInfoId")
    public R commentByInfoId(@RequestParam("infoId") String infoId,
                             @RequestParam("current") Long current) {
        IPage<CommentVO> page = new Page<>(current, 10);
        return R.data(inforService.commentByInfoId(page, Long.valueOf(infoId)));

    }

    @ApiOperation(value = "查询资讯评论")
    @GetMapping("/commentListByInfoId")
    public R commentListByInfoId(@RequestParam("infoId") String infoId) {
        return R.data(inforService.commentListByInfoId(Long.valueOf(infoId)));

    }

    @ApiOperation(value = "搜索资讯")
    @GetMapping("/search")
    public R search(@RequestParam("val") String val,
                    @RequestParam("current") Long current) {
        IPage<InforVO> page = new Page<>(current, 10);
        return R.data(inforService.search(page, val));
    }

    @ApiOperation(value = "教师搜索资讯")
    @GetMapping("/teaSearch")
    public R teaSearch(@RequestParam("val") String val,
                       @RequestParam("current") Long current,
                       @ApiIgnore HttpSession session) {
        Long teaId = (Long) session.getAttribute("teaId");
        if (teaId == null) {
            return R.error(403, "无访问权限");
        }
        IPage<InforVO> page = new Page<>(current, 7);
        return R.data(inforService.teaSearch(page, val, teaId));
    }

    @ApiOperation(value = "点赞")
    @PostMapping("/approval")
    public R approval(@RequestParam("infoId") String infoId,
                      @ApiIgnore HttpSession session) {

        Long stuId = (Long) session.getAttribute("stuId");
        Long teaId = (Long) session.getAttribute("teaId");
        Long userId = 0L;
        if (stuId != null) {
            userId = stuId;
        }else if(teaId != null){
            userId = teaId;
        }else {
            return R.error("无访问权限");
        }

        return R.data(inforService.approval(Long.valueOf(infoId), userId));
    }

    @ApiOperation(value = "取消点赞")
    @PostMapping("/cancelApproval")
    public R cancelApproval(@RequestParam("infoId") String infoId,
                            @ApiIgnore HttpSession session) {

        Long stuId = (Long) session.getAttribute("stuId");
        Long teaId = (Long) session.getAttribute("teaId");
        Long userId = 0L;
        if (stuId != null) {
            userId = stuId;
        }else if(teaId != null){
            userId = teaId;
        }else {
            return R.error("无访问权限");
        }

        return R.data(inforService.cancelApproval(Long.valueOf(infoId), userId));

    }

    @ApiOperation(value = "多删除资讯")
    @PostMapping("/deleted")
    public R deleted(@RequestBody List<String> infoId) {
        List<Long> iId = new ArrayList<>();
        for (int i=0;i<infoId.size();i++) {
            iId.add(Long.valueOf(infoId.get(i)));
        }
        inforService.deleted(iId);
        return R.ok("删除资讯成功");
    }

    @ApiOperation(value = "修改资讯")
    @PostMapping("/update")
    public R update(@RequestBody InforDTO inforDTO) {
        inforService.update(inforDTO);
        return R.ok("修改资讯成功");
    }

    @ApiOperation(value = "修改资讯逻辑删除")
    @PostMapping("/remoteUpdateInfo")
    public void remoteUpdateInfo(@RequestBody InforDTO inforDTO) {
        inforService.remoteUpdateInfo(inforDTO);
    }

    @GetMapping("/teaNoticeInfo")
    List<InforVO> teaNoticeInfo(@RequestParam("teaId") Long teaId) {
        return inforService.teaNoticeInfo(teaId);
    }

    @SneakyThrows
    @PostMapping("/inforFile")
    @ApiOperation(value = "上传图片")
    public R inforFile(@RequestParam(value = "file") MultipartFile file, @ApiIgnore HttpSession session){
        Long sessionId = (Long) session.getAttribute("sessionId");
        if (sessionId == null){
            return R.error(403, "无访问权限");
        }
        if(file.isEmpty()) {
            return R.error();
        }

        try {
            String fileUrl=qiniuService.saveImage(file);
            SampleAip sampleAip = new SampleAip(APP_ID, API_KEY, SECRET_KEY);
            int conclusionTypeI = Sample.sampleImage(sampleAip.getAip(), fileUrl);
            if (conclusionTypeI != 1) {
                String[] path = fileUrl.split("/");
                String key = path[3];
                try {
                    qiniuService.deleteFile(key);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return R.error("图片存在违规");
            }
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

//    @SneakyThrows
//    @PostMapping("/inforFile")
//    @ApiOperation(value = "上传图片")
//    public String inforFile(@RequestParam(value = "file") MultipartFile file, @ApiIgnore HttpSession session){
//        Long sessionId = (Long) session.getAttribute("sessionId");
//        if (sessionId == null){
//            return "无访问权限";
//        }
//        return inforService.inforFile(file);
//    }

}