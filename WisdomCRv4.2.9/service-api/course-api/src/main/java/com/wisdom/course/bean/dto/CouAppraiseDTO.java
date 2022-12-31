package com.wisdom.course.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@Accessors(chain = true)
@ApiModel(value = "CouAppraiseDTO",description = "课程评价DTO对象")
public class CouAppraiseDTO {

    @ApiModelProperty(value = "主键ID，前端无需提供")
    @Null(message = "非法参数")
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程ID")
    @NotNull(message = "课程ID不能为空")
    private Long couId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "学生ID")
    @NotNull(message = "学生ID不能为空")
    private Long stuId;

    @ApiModelProperty(value = "评价内容")
    @NotNull(message = "评价内容不能为空")
    private String couAppraise;

    @ApiModelProperty(value = "评价分数0-5之间的整数")
    @NotNull(message = "评价分数不能为空")
    private int evaluateScore;



}
