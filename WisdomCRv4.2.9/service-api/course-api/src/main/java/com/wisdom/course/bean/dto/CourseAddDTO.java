package com.wisdom.course.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "/CourseAddDTO",description = "添加课程DTO对象")
public class CourseAddDTO implements Serializable {
    @ApiModelProperty("课程ID，前端无需提供")
    @Null(message = "非法参数")
    private Long couId;

    @ApiModelProperty(value = "教师ID，前端无需提供")
    @Null(message = "非法参数")
    private Long teaId;

    @ApiModelProperty(value = "课程名称")
    @NotEmpty(message = "课程名称不能为空")
    private String couName;

    @ApiModelProperty(value = "课程介绍")
    @Size(max=250,message="课程介绍不能超过250字")
    private String couIntroduction;

    @ApiModelProperty(value = "课程价格")
    @NotNull(message = "课程价格不能为空")
    private Integer couPrice;

    @ApiModelProperty(value = "课程类型")
    @NotNull(message = "课程类型不能为空")
    private Integer couType;

    @ApiModelProperty(value = "课程封面")
    private String couPic;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程类别")
    private Long couTypeId;
}
