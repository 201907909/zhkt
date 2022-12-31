package com.wisdom.course.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "/CouAndTypeDTO",description = "添加课程类别DTO对象")
public class CouAndTypeDTO implements Serializable {
    @ApiModelProperty(value = "主键ID，前端无需提供")
    @Null(message = "非法参数")
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程类别ID")
    @NotNull(message = "课程类别ID不能为空")
    private Long couTypeId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程ID")
    @NotNull(message = "课程ID不能为空")
    private Long couId;
}
