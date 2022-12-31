package com.wisdom.user.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WisdomCR
 * @since 2022/11/10
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "StuCoursePro",description = "学生在班级的学习进度对象")
@TableName(value = "stu_cou_progress")
public class StuCoursePro implements Serializable {
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "学生ID")
    private Long stuId;

    @ApiModelProperty(value = "课程ID")
    private Long couId;

    @ApiModelProperty(value = "章节ID")
    private Long chaId;

    @ApiModelProperty(value = "父章节ID")
    private Long faChaId;

    @ApiModelProperty(value = "最新观看时长,单位：秒")
    private Long startTime;

    @ApiModelProperty(value = "最长观看时长,单位：秒")
    private Long endTime;

    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private Integer deleted;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
