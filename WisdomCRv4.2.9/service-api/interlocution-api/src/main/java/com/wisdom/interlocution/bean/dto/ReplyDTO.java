package com.wisdom.interlocution.bean.dto;

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
@ApiModel(value = "ReplyDTO对象",description = "回答对象")
public class ReplyDTO {

    @ApiModelProperty(value = "话题/问题ID")
    private Long queId;


    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @ApiModelProperty(value = "角色（0：教师，1：学生）")
    private Integer role;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "被回复的问题ID")
    private Long answeredTqId;

    @ApiModelProperty(value = "内容")
    private String content;
}
