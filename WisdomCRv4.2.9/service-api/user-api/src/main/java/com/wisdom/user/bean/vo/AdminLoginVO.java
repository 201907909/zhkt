package com.wisdom.user.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@Accessors(chain = true)
@ApiModel(value = "AdminLoginVO",description = "管理员登录VO对象")
public class AdminLoginVO {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("账号")
    @NotEmpty(message = "账号不能为空")
    @Email(message = "邮箱格式错误")
    private String account;

    @ApiModelProperty("密码")
    @NotEmpty(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "角色，2为管理员")
    private int role;
}
