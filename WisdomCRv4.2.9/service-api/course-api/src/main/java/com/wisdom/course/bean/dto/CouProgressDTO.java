package com.wisdom.course.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "/CouProgressDTO",description = "课程学习进度DTO对象")
public class CouProgressDTO implements Serializable {
    @ApiModelProperty(value = "课程ID List")
    List<Long> couIds;

    @ApiModelProperty(value = "课程ID")
    Long couId;

    @ApiModelProperty(value = "学生ID")
    @NotNull(message = "学生ID不能为空")
    Long stuId;
}
