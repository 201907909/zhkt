package com.wisdom.course.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "/CouUpTypeDTO",description = "修改课程类别DTO对象")
public class CouUpTypeDTO implements Serializable {
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程类别ID")
    @NotNull(message = "课程类别ID不能为空")
    private Long couTypeId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程ID")
    @NotNull(message = "课程ID不能为空")
    private Long couId;
}
