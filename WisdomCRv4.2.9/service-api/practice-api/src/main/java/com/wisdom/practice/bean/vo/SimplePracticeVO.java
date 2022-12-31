package com.wisdom.practice.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
 * @author WisdomCR
 * @since 2022/11/3
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "SimplePracticeVO", description = "指定实践简单VO对象")
public class SimplePracticeVO {
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "实践ID")
    private Long praId;

    @ApiModelProperty(value = "实践名称")
    private String praName;

    @ApiModelProperty(value = "教师名称")
    private String teaName;

    @ApiModelProperty(value = "实践地点")
    private String praSite;

    @ApiModelProperty(value = "实践内容")
    private String praMain;

    @ApiModelProperty(value = "实践宣传图")
    private String praImgName;

    @ApiModelProperty(value = "实践宣传图IO")
    private String praImgIO;

    @ApiModelProperty(value = "实践创建时间")
    private Date createTime;

    @ApiModelProperty(value = "剩余时间")
    private String surplusTime;     // 实践已结束则为空

    @ApiModelProperty(value = "报名状态")
    private Integer state;      // 0代表参加，1代表没参加

    @ApiModelProperty(value = "提交状态")
    private Integer isEndS;      // 0代表没提交，1代表已提交

    @ApiModelProperty(value = "结束状态")
    private Integer isEndP;      // 0代表没结束，1代表已结束
}
