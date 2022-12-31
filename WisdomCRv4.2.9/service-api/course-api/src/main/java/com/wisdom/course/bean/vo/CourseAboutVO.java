package com.wisdom.course.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author WisdomCR
 * @since 2022-10-30
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "CourseAboutVO",description = "课程大概信息VO对象")
public class CourseAboutVO implements Serializable {
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "课程ID")
    private Long couId;

    @ApiModelProperty(value = "课程名称")
    private String couName;

    @ApiModelProperty(value = "课程类型")
    private Integer couType;

    @ApiModelProperty(value = "课程封面流")
    private String couPicIO;

    @ApiModelProperty(value = "课程封面")
    private String couPic;

    @ApiModelProperty(value = "课程类别")
    private String typeName;

    @ApiModelProperty(value = "课程进度")
    private Long couProgress;

    @ApiModelProperty(value = "课程时长")
    private Long couTotalProgress;

    @ApiModelProperty(value = "进度百分百")
    private Long couProgressBfb;

    @ApiModelProperty(value = "课程父类别")
    private String parentTypeName;
}
