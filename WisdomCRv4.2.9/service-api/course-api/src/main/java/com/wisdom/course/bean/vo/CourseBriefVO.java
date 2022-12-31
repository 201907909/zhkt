package com.wisdom.course.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
@ApiModel(value = "CourseBriefVO",description = "课程简略信息VO对象")
public class CourseBriefVO implements Serializable {
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程ID")
    private Long couId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "教师ID")
    private Long teaId;

    @ApiModelProperty(value = "课程名称")
    private String couName;

    @ApiModelProperty(value = "教师名称")
    private String teaName;

    @ApiModelProperty(value = "课程类型")
    private Integer couType;

    @ApiModelProperty(value = "课程封面流")
    private String couPicIO;

    @ApiModelProperty(value = "课程封面")
    private String couPic;

    @ApiModelProperty(value = "课程父类别")
    private String parentTypeName;

    @ApiModelProperty(value = "是否被添加")
    private Integer state = 0;
}
