package com.wisdom.user.bean.dto;

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
 * @since 2022-10-17
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "ScoreQueryDTO",description = "查看积分DTO对象")
public class ScoreQueryDTO implements Serializable {
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "学生ID")
    @NotNull(message = "学生ID不能为空")
    private Long stuId;

    @ApiModelProperty(value = "积分类别")
    @NotNull(message = "积分类别不能为空")
    private Integer scoreType;
}
