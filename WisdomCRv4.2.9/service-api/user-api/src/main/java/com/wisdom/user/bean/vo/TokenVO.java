package com.wisdom.user.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 令牌
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "TokenVO",description = "学生登录VO对象")
public class TokenVO {
    @ApiModelProperty(value = "主键ID")
    private Long userId;

    @ApiModelProperty(value = "Token")
    private String token;
}
