package com.wisdom.course.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ClickVO {

    @ApiModelProperty(value = "点赞数")
    private Long recommendNum;

    @ApiModelProperty(value = "是否点赞，0未点，1点赞")
    private int isClick;
}