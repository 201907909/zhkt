package com.wisdom.user.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "UserLoginDTO",description = "用户登录DTO对象")
public class UserLoginDTO implements Serializable {
    @ApiModelProperty("账号")
    @NotEmpty(message = "账号不能为空")
    @Email(message = "邮箱格式错误")
    private String account;

    @ApiModelProperty("密码")
    @NotEmpty(message = "密码不能为空")
    private String password;
}
