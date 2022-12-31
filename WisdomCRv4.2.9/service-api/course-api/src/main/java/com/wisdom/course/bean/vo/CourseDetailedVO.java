package com.wisdom.course.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author WisdomCR
 * @since 2022-10-30
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "CourseDetailedVO",description = "课程详细信息VO对象")
public class CourseDetailedVO implements Serializable {
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程ID")
    private Long couId;

    @ApiModelProperty(value = "课程名称")
    private String couName;

    @ApiModelProperty(value = "教师名称")
    private String teaName;

    @ApiModelProperty(value = "类别名称")
    private String typeName;

    @ApiModelProperty(value = "父类别名称")
    private String parentTypeName;

    @ApiModelProperty(value = "课程介绍")
    private String couIntroduction;

    @ApiModelProperty(value = "课程价格")
    private Integer couPrice;

    @ApiModelProperty(value = "课程类型")
    private Integer couType;

    @ApiModelProperty(value = "课程封面流")
    private String couPicIO;

    @ApiModelProperty(value = "课程封面")
    private String couPic;

    @ApiModelProperty(value = "课程章节数")
    private Integer couCataNum;

    @ApiModelProperty(value = "课程收藏数")
    private Long couCollNum;

    @ApiModelProperty(value = "课程分享数")
    private Long couShareNum;

    @ApiModelProperty(value = "点击量")
    private Long clicks;

    @ApiModelProperty(value = "购买量")
    private Long purchaseNum;

    @ApiModelProperty(value = "评价量")
    private Long evaluateNum;

    @ApiModelProperty(value = "点赞量")
    private Long recommendNum;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程类别ID")
    private Long typeId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程父类别ID")
    private Long parentTypeId;

    @ApiModelProperty(value = "是否点赞，0为没赞，1为已赞")
    private Integer isClick;

    @ApiModelProperty(value = "是否点赞，0为未收藏，1为已收藏")
    private Integer isCollection;

    @ApiModelProperty(value = "是否购买，0为未购买，1为已购买")
    private Integer isBuy;

    @ApiModelProperty(value = "是否加入购物车，0为未加入，1为已加入")
    private Integer isAddCart;

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

    @ApiModelProperty(value = "推荐课程")
    private List<CouVO> couRelatedVOList;
}
