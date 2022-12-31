package com.wisdom.user.controller;

import com.wisdom.base.util.R;
import com.wisdom.user.service.ColletionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/colletion")
@Api(tags = "收藏接口")
@CrossOrigin
public class ColletionController {

    @Autowired
    private ColletionService colletionService;

    /**
     * 查看所有收藏
     */
    @GetMapping("/getAllColletion")
    @ApiOperation(value = "查看所有收藏")
    public R getAllColletion(@ApiIgnore HttpSession session){
        Long stuId = (Long) session.getAttribute("stuId");
        return R.data(colletionService.getAllColletion(stuId));
    }

    /**
     * 删除收藏
     */
    @PostMapping("/deleted")
    @ApiOperation(value = "删除收藏")
    public R deleted(@ApiIgnore HttpSession session,
                     @RequestParam("couId") Long couId) {
        Long stuId = (Long) session.getAttribute("stuId");
        colletionService.deleted(stuId, couId);
        return R.ok();
    }

    /**
     * 添加收藏
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加收藏")
    public R add(@ApiIgnore HttpSession session,
                 @RequestParam("couId") Long couId) {
        Long stuId = (Long) session.getAttribute("stuId");
        colletionService.add(stuId, couId);
        return R.ok();
    }

    /**
     * 查看收藏详情
     */
    @GetMapping("/conDetailByCouId")
    @ApiOperation(value = "查看收藏详情")
    public R conDetailByCouId(@RequestParam("couId") Long couId) {
        return R.data(colletionService.conDetailByCouId(Long.valueOf(couId)));
    }

    @GetMapping("/isCollection")
    public Integer isCollection(@RequestParam("stuId") Long stuId, @RequestParam("couId") Long couId) {
        return colletionService.isCollection(stuId, couId);
    }

}
