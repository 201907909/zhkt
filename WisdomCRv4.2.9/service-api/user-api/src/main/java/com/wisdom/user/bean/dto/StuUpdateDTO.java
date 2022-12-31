package com.wisdom.user.bean.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import javax.validation.constraints.Email;
import javax.validation.constraints.Null;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "StuUpdateDTO",description = "修改学生信息DTO对象")
public class StuUpdateDTO implements Serializable {
    @ApiModelProperty("主键ID，前端无需提供")
    @Null(message = "非法数据")
    private Long id;

    @ApiModelProperty("学生姓名")
    private String stuName;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty(value = "性别")
    private Integer gender;

    @ApiModelProperty(value = "新头像")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String newImage;

    @ApiModelProperty(value = "旧头像")
    private String oldImage;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("学习时长")
    private Long stuStudyTime;
}
