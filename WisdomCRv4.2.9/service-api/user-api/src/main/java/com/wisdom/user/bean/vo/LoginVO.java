package com.wisdom.user.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "LoginVO",description = "用户登录VO对象")
public class LoginVO implements Serializable {
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "学生ID")
    private Long userId;
}
