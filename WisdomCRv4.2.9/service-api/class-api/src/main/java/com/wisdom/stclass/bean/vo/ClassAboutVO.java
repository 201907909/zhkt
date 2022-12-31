package com.wisdom.stclass.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;

/**
 * @author WisdomCR
 * @since 2022-10-30
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "ClassAboutVO",description = "班级大概信息VO对象")
public class ClassAboutVO implements Serializable {
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "班级ID")
    private Long claId;

    @ApiModelProperty(value = "班级名称")
    private String className;

    @ApiModelProperty(value = "班级LogoIO")
    private String classLogoIO;

    @ApiModelProperty(value = "班级Logo")
    private String classLogo;

    @ApiModelProperty(value = "班级详情")
    private String classDetails;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
