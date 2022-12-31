package com.wisdom.course.bean.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "CouAboutClassVO",description = "班级需要课程大概信息VO对象")
public class CouAboutClassVO implements Serializable {
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程ID")
    private Long couId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "教师ID")
    private Long teaId;

    @ApiModelProperty(value = "教师名称")
    private String teaName;

    @ApiModelProperty(value = "课程名称")
    private String couName;

    @ApiModelProperty(value = "课程封面")
    private String couPic;

    @ApiModelProperty(value = "点赞数")
    private Long recommendNum;

    @ApiModelProperty(value = "课程收藏数")
    private Long couCollNum;

    @ApiModelProperty(value = "课程总时长")
    private Long couProgressTotal;

    @ApiModelProperty(value = "课程第一个视频")
    private String couFirstCha;
}
