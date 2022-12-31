package com.wisdom.stclass.bean.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author WisdomCR
 * @since 2022-10-30
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "ClassUpdateDTO",description = "修改班级DTO对象")
public class ClassUpdateDTO implements Serializable {
    @ApiModelProperty(value = "主键ID")
    @NotNull(message = "主键ID不能为空")
    private Long id;

    @ApiModelProperty(value = "班级名称")
    private String className;

    @ApiModelProperty(value = "新班级Logo")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String newClassLogo;

    @ApiModelProperty(value = "旧班级Logo")
    private String oldClassLogo;

    @ApiModelProperty(value = "班级详情")
    @Size(max=200,message="班级详情不能超过200字")
    private String classDetails;
}
