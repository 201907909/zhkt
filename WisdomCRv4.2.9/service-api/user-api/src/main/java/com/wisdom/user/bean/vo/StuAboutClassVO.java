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
@ApiModel(value = "StuAboutClassVO",description = "班级需要学生大概信息VO对象")
public class StuAboutClassVO implements Serializable {
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "学生ID")
    private Long stuId;

    @ApiModelProperty(value = "学生姓名")
    private String stuName;

    @ApiModelProperty(value = "头像")
    private String image;

    @ApiModelProperty(value = "邮箱")
    private String email;
}
