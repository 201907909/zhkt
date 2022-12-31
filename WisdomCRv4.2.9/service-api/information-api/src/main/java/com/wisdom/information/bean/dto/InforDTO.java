package com.wisdom.information.bean.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "InforDTO对象",description = "资讯DTO对象")
public class InforDTO {

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "资讯ID")
    private Long infoId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long teaId;

    @ApiModelProperty(value = "资讯标题")
    private String infoTitle;

    @ApiModelProperty(value = "资讯内容")
    private String infoMain;

    @ApiModelProperty(value = "资讯作者")
    private String infoAuthor;

    @ApiModelProperty(value = "逻辑删除")
    private int deleted;

    @ApiModelProperty(value = "图片")
    private String infoImage;
}
