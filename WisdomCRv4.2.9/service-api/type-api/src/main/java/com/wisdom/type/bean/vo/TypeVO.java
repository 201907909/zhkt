package com.wisdom.type.bean.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TypeVO {
    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "子类别ID")
    private Long couTypeId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "父类别ID")
    private Long parentTypeId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "实践类别ID")
    private Long praTypeId;

    @ApiModelProperty(value = "类别名称")
    private String typeName;

    @ApiModelProperty(value = "logo")
    private String img;
}
