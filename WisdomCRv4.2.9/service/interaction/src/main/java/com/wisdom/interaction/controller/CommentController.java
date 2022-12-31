package com.wisdom.interaction.controller;

import com.wisdom.base.util.R;
import com.wisdom.comment.bean.dto.CommentDTO;
import com.wisdom.interaction.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@Api(tags = "评论管理接口")
@CrossOrigin
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 发表评论
     */
    @PostMapping("/add")
    @ApiOperation(value = "发表评论")
    public R add(@RequestBody CommentDTO commentDTO) {
        Long userId = commentDTO.getUserId();
        if (userId == null) {
            return R.error(403, "无访问权限");
        }
        return commentService.add(commentDTO);
    }
}
