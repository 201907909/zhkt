package com.wisdom.interlocution.bean.dto;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "InterlocutionDTO对象",description = "发布问题所需的对象")
public class InterlocutionDTO {

    @ApiModelProperty(value = "话题/问题ID")
    private Long queId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @ApiModelProperty(value = "是否被采纳")
    private Integer isSelect;

    @ApiModelProperty(value = "图片")
    private List<String> imgs;

    @ApiModelProperty(value = "图片")
    private String img;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "标题")
    private String title;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "大类")
    private Long maxTypeId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "小类")
    private Long minTypeId;

    @ApiModelProperty(value = "逻辑删除")
    private int deleted;


}
