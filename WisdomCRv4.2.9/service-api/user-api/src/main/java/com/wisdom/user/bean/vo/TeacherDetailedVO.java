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
@ApiModel(value = "TeacherDetailedVO",description = "教师详情VO对象")
public class TeacherDetailedVO implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "教师Id")
    private Long teaId;

    @ApiModelProperty(value = "教师姓名")
    private String teaName;

    @ApiModelProperty(value = "性别")
    private int gender;

    @ApiModelProperty(value = "头像")
    private String image;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty("课程数")
    private Integer courseNum;
}
