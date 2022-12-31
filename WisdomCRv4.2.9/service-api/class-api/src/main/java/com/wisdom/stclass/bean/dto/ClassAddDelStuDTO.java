package com.wisdom.stclass.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;

/**
 * @author WisdomCR
 * @since 2022-10-30
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "ClassAddDelStuDTO",description = "班级添加删除学生DTO对象")
public class ClassAddDelStuDTO implements Serializable {
    @ApiModelProperty("主键ID，前端无需提供")
    @Null(message = "非法参数")
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("班级ID")
    @NotNull(message = "班级ID不能为空")
    private Long classId;

    @ApiModelProperty("学生ID，前端无需提供")
    @Null(message = "非法参数")
    private Long stuId;
}
