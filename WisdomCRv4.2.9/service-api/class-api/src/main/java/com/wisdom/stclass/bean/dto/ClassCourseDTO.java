package com.wisdom.stclass.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotNull;

/**
 * @author WisdomCR
 * @since 2022-10-30
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "ClassAddDelStuDTO",description = "班级添加删除学生DTO对象")
public class ClassCourseDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("班级ID")
    @NotNull(message = "班级ID不能为空")
    private Long classId;

    @ApiModelProperty("开始页码")
    @NotNull(message = "开始页码不能为空")
    private Integer startIndex;

    @ApiModelProperty("数据个数")
    @NotNull(message = "数据个数不能为空")
    private Integer pageSize;
}
