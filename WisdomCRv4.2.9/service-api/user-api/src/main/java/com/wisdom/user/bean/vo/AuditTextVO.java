package com.wisdom.user.bean.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@ApiModel(value = "AuditTextVO",description = "审核文本VO对象")
public class AuditTextVO {

    @ApiModelProperty("主键ID，前端无需提供")
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("审核的雪花Id")
    private Long auditId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("发布者id")
    private Long userId;

    @ApiModelProperty("发布者")
    private String userName;

    @ApiModelProperty("发布者身份(教师：0，学生：1)")
    private int role;

    @ApiModelProperty("文本类型(0:标题，1:正文)")
    private int textType;

    @ApiModelProperty("审核结果")
    private int auditResult;

    @ApiModelProperty("审核文本")
    private String auditText;

    @ApiModelProperty("审核源（0:资讯，1:资讯评论， 2:问题，3:回答，4:课程，5:头像，6:课程评论）")
    private int auditSource;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

}
