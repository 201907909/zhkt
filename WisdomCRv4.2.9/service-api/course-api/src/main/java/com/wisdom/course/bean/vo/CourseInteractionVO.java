package com.wisdom.course.bean.vo;

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
@ApiModel(value = "CourseInteractionVO",description = "课程互动详情VO对象")
public class CourseInteractionVO implements Serializable {
    @ApiModelProperty(value = "是否收藏")
    private Integer collect;

    @ApiModelProperty(value = "是否加入购物车")
    private Integer cart;

    @ApiModelProperty(value = "是否购买")
    private Integer have;

    @ApiModelProperty(value = "是否点赞")
    private Integer recommend;

    @ApiModelProperty(value = "是否评价")
    private Integer evaluate;

    @ApiModelProperty(value = "是否分享")
    private Integer share;

    @ApiModelProperty(value = "插入时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
