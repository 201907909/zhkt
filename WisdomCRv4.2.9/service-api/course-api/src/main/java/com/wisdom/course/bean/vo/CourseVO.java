package com.wisdom.course.bean.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "CourseVO",description = "课程详细信息VO对象")
public class CourseVO implements Serializable {
    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程ID")
    private Long couId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "教师ID")
    private Long teaId;

    @ApiModelProperty(value = "课程名称")
    private String couName;

    @ApiModelProperty(value = "课程介绍")
    private String couIntroduction;

    @ApiModelProperty(value = "课程价格")
    private Integer couPrice;

    @ApiModelProperty(value = "课程类型")
    private Integer couType;

    @ApiModelProperty(value = "课程章节数")
    private Integer couCataNum;

    @ApiModelProperty(value = "课程收藏数")
    private Long couCollNum;

    @ApiModelProperty(value = "课程分享数")
    private Long couShareNum;

    @ApiModelProperty(value = "点击量")
    private Long clicks;

    @ApiModelProperty(value = "购买量")
    private Long purchaseNum;

    @ApiModelProperty(value = "评价量")
    private Long evaluateNum;

    @ApiModelProperty(value = "点赞数")
    private Long recommendNum;

    @ApiModelProperty(value = "课程封面IO")
    private String couPicIO;

    @ApiModelProperty(value = "课程封面")
    private String couPic;

    @ApiModelProperty(value = "教师名字")
    private String teaName;


}
