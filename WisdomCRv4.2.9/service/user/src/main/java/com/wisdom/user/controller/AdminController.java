package com.wisdom.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisdom.base.util.R;
import com.wisdom.user.bean.dto.*;
import com.wisdom.user.bean.vo.AdminLoginVO;
import com.wisdom.user.bean.vo.AuditImgVO;
import com.wisdom.user.bean.vo.AuditTextVO;
import com.wisdom.user.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/admin")
@Api(tags = "管理员接口")
@CrossOrigin
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    @ApiOperation(value="管理员登录")
    public R login(@Validated @RequestBody AdminLoginDTO adminLoginDTO, @ApiIgnore HttpSession session){
        AdminLoginVO admin = adminService.login(adminLoginDTO);
        if (admin == null) {
            return R.error("账号密码错误");
        }else {
            return R.data(admin);
        }
    }

    /**
     * 文本复审
     */
    @GetMapping("/reviewText")
    @ApiOperation(value="文本复审")
    public R reviewText(@RequestParam("current") Long current) {
        IPage<AuditTextVO> page = new Page<>(current, 7);
        return R.data(adminService.reviewText(page));
    }

    /**
     * 文本历史
     */
    @GetMapping("/historyText")
    @ApiOperation(value="文本历史")
    public R historyText(@RequestParam("current") Long current) {
        IPage<AuditTextVO> page = new Page<>(current, 7);
        return R.data(adminService.historyText(page));
    }

    /**
     * 图片复审
     */
    @GetMapping("/reviewImg")
    @ApiOperation(value="图片复审")
    public R reviewImg(@RequestParam("current") Long current) {
        IPage<AuditImgVO> page = new Page<>(current, 7);
        return R.data(adminService.reviewImg(page));
    }

    /**
     * 图片历史
     */
    @GetMapping("/historyImg")
    @ApiOperation(value="图片历史")
    public R historyImg(@RequestParam("current") Long current) {
        IPage<AuditImgVO> page = new Page<>(current, 7);
        return R.data(adminService.historyImg(page));
    }

    /**
     * 通过
     */
    @PostMapping("/auditPass")
    @ApiOperation(value="通过")
    public R auditPass(@RequestBody AuditPassDTO auditPassDTO) {
        adminService.auditPass(auditPassDTO.getId(), auditPassDTO.getAuditId(), auditPassDTO.getAuditType());
        return R.ok("通过");
    }

    /**
     * 驳回
     */
    @GetMapping("/overruled")
    @ApiOperation(value="驳回")
    public R overruled(@RequestParam("id") String id,
                       @RequestParam("auditId") String auditId,
                       @RequestParam("auditType") int auditType) {
        adminService.overruled(Long.valueOf(id), Long.valueOf(auditId), auditType);
        return R.ok("已驳回");
    }

    /**
     * 文本安全
     */
    @GetMapping("/textSecurity")
    @ApiOperation(value="文本安全")
    public R textSecurity(@RequestParam("current") Long current, @RequestParam(value = "val", required = false, defaultValue = "") String val) {
        IPage<AuditTextVO> page = new Page<>(current, 7);
        return R.data(adminService.textSecurity(page, val));
    }

    /**
     * 图像安全
     */
    @GetMapping("/imgSecurity")
    @ApiOperation(value="图像安全")
    public R imgSecurity(@RequestParam("current") Long current) {
        IPage<AuditImgVO> page = new Page<>(current, 7);
        return R.data(adminService.imgSecurity(page));
    }

    /**
     * 不通过
     */
    @PostMapping("/eliminate")
    @ApiOperation(value="不通过")
    public R eliminate(@RequestBody AuditNoPassDTO auditNoPassDTO) {
        adminService.eliminate(auditNoPassDTO);
        return R.ok("不通过");
    }


    @PostMapping("/addAuditText")
    void addAuditText(@RequestBody AuditTextDTO auditTextDTO) {
        adminService.addAuditText(auditTextDTO);
    }

    @PostMapping("/addAuditImg")
    void addAuditImg(@RequestBody AuditImgDTO auditImgDTO) {
        adminService.addAuditImg(auditImgDTO);
    }
}
