package com.wisdom.course.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "CourseAddVO",description = "添加课程VO对象")
public class CourseAddVO implements Serializable {
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程ID")
    private Long couId;

    @ApiModelProperty(value = "课程名称")
    private String couName;

    @ApiModelProperty(value = "课程价格")
    private Integer couPrice;

    @ApiModelProperty(value = "课程类型")
    private Integer couType;
}
