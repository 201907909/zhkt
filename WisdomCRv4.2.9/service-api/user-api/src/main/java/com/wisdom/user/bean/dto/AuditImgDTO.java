package com.wisdom.user.bean.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "AuditImgDTO",description = "审核图像DTO对象")
public class AuditImgDTO {

    @ApiModelProperty("主键ID，前端无需提供")
    private Long id;

    @ApiModelProperty("审核的雪花Id")
    private Long auditId;

    @ApiModelProperty("发布者id")
    private Long userId;

    @ApiModelProperty("发布者")
    private String userName;

    @ApiModelProperty("审核结果")
    private int auditResult;

    @ApiModelProperty("发布者身份(教师：0，学生：1)")
    private int role;

    @ApiModelProperty("审核图像")
    private String auditImg;

    @ApiModelProperty("审核源（0:资讯，1:资讯评论， 2:问题，3:回答，4:课程，5:头像，6:课程评论）")
    private int auditSource;

}
