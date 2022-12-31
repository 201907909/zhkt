package com.wisdom.course.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "CouVO",description = "CouVO对象")
public class CouVO {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程ID")
    private Long couId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "教师ID")
    private Long teaId;

    @ApiModelProperty(value = "课程名称")
    private String couName;

    @ApiModelProperty(value = "课程介绍")
    private String couIntroduction;

    @ApiModelProperty(value = "课程封面")
    private String couPic;

    @ApiModelProperty(value = "教师名字")
    private String teaName;

    @ApiModelProperty(value = "课程章节数")
    private Integer couCataNum;

    @ApiModelProperty(value = "课程价格")
    private Integer couPrice;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "大类Id")
    private Long maxTypeId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "小类Id")
    private Long minTypeId;

    @ApiModelProperty(value = "大类名称")
    private String maxTypeName;

    @ApiModelProperty(value = "小类名称")
    private String minTypeName;

}
