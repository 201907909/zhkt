package com.wisdom.practice.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author WisdomCR
 * @since 2022/11/3
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "PraClassVO", description = "班级实践VO对象")
public class PraClassVO {
    @ApiModelProperty(value = "参与人数")
    private Integer value;

    @ApiModelProperty(value = "班级名称")
    private String name;
}
