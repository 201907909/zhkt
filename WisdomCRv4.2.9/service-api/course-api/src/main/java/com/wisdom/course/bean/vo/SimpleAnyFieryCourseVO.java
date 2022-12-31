package com.wisdom.course.bean.vo;

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
 * @since 2022-10-30
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "SimpleAnyFieryCourseVO",description = "简单各层面热度课程VO对象")
public class SimpleAnyFieryCourseVO implements Serializable {
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程ID")
    private Long couId;

    @ApiModelProperty(value = "课程名称")
    private String couName;

    @ApiModelProperty(value = "教师名称")
    private String teaName;

    @ApiModelProperty(value = "课程封面")
    private String couPicName;

    @ApiModelProperty(value = "课程封面流")
    private String couPicIO;

    @ApiModelProperty(value = "创建时间")
    private Date createTimeE;

    @ApiModelProperty(value = "创建时间")
    private String createTime;
}
