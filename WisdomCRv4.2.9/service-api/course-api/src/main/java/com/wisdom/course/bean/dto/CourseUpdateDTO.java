package com.wisdom.course.bean.dto;

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

@Data
@Accessors(chain = true)
@ApiModel(value = "CourseUpdateDTO",description = "修改课程DTO对象")
public class CourseUpdateDTO implements Serializable {
    @ApiModelProperty(value = "主键ID")
    @NotNull(message = "主键ID不能为空")
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程ID")
    @NotNull(message = "课程ID不能为空")
    private Long couId;

    @ApiModelProperty(value = "课程名称")
    private String couName;

    @ApiModelProperty(value = "课程介绍")
    @Size(max=250,message="课程介绍不能超过250字")
    private String couIntroduction;

    @ApiModelProperty(value = "课程价格")
    private Integer couPrice;

    @ApiModelProperty(value = "课程类型")
    private Integer couType;

    @ApiModelProperty(value = "新课程封面")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String newCouPic;

    @ApiModelProperty(value = "旧课程封面")
    private String oldCouPic;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程类别")
    private Long couTypeId;
}
