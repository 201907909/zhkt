package com.wisdom.course.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author WisdomCR
 * @since 2022-10-30
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "CouAllNumVO",description = "课程所有共享数据量VO对象")
public class CouAllNumVO implements Serializable {
    @ApiModelProperty(value = "主键ID")
    private Long id;

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

    @ApiModelProperty(value = "课程评价数")
    private Long evaluateNum;

    @ApiModelProperty(value = "点赞数")
    private Long recommendNum;
}
