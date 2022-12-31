package com.wisdom.stclass.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author WisdomCR
 * @since 2022-10-30
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "ClassAddDelStuSDTO",description = "班级批量添加删除学生DTO对象")
public class ClassAddDelStuSDTO implements Serializable {
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("班级ID")
    @NotNull(message = "班级ID不能为空")
    private Long classId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("学生ID List")
    @NotEmpty(message = "学生ID 不能为空")
    private Long stuIds[];
}
