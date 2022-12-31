package com.wisdom.durations.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Durations",description = "时长对象")
@TableName(value = "durations")
public class Durations implements Serializable {
    private static final long serialVersionUID=1L;

    /*所有表*/
    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /*所有表*/
    @ApiModelProperty(value = "学生ID")
    private Long stuId;

    /*所有表*/
    @ApiModelProperty(value = "课程ID")
    private Long couId;

    /*学习进度表(班级课程)*/
    @ApiModelProperty(value = "班级ID")
    private Long classId;

    /*
     * 学习进度表(班级课程)
     * 学生-课程学习进度(自选课程)
     */
    @ApiModelProperty(value = "章或节ID")
    private Long chaId;

    /*
     * 学习进度表(班级课程)
     * 学生-课程学习进度(自选课程)
     */
    @ApiModelProperty(value = "父章ID")
    private Long faChaId;

    /*
     * 学习进度表(班级课程)
     * 学生-课程学习进度(自选课程)
     */
    @ApiModelProperty(value = "开始时间")
    private Long startTime;

    /*
     * 学习进度表(班级课程)
     * 学生-课程学习进度(自选课程)
     */
    @ApiModelProperty(value = "结束时间")
    private Long endTime;

    /*学生-课程关联表*/
    @ApiModelProperty(value = "每日学习时长")
    private Long dailyDuration;

    /*历史学习时长表*/
    @ApiModelProperty(value = "学习时长")
    private Long dailyTime;

    /*所有表*/
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    /*所有表*/
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
