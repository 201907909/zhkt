package com.wisdom.stclass.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;

/**
 * @author WisdomCR
 * @since 2022-10-30
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "ClassAddDelCouDTO",description = "班级添加删除课程DTO对象")
public class ClassAddDelCouDTO implements Serializable {
    @ApiModelProperty("主键ID，前端无需提供")
    private Long id;

    @ApiModelProperty("班级ID")
    private Long classId;

    @ApiModelProperty("课程ID，前端无需提供")
    private Long couId;
}
