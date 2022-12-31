package com.wisdom.course.bean.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;

/**
 * @author WisdomCR
 * @since 2022-10-30
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "CouInteractionUpDTO",description = "课程互动信息修改DTO对象")
public class CouInteractionUpDTO implements Serializable {
    @ApiModelProperty(value = "课程ID")
    @NotNull(message = "课程ID不能为空")
    private Long couId;

    @ApiModelProperty(value = "学生ID")
    @NotNull(message = "学生ID不能为空")
    private Long stuId;

    @ApiModelProperty(value = "是否收藏")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Null(message = "非法参数")
    private Integer collect;

    @ApiModelProperty(value = "是否加入购物车")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Null(message = "非法参数")
    private Integer cart;

    @ApiModelProperty(value = "是否购买")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Null(message = "非法参数")
    private Integer have;

    @ApiModelProperty(value = "是否点赞")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Null(message = "非法参数")
    private Integer recommend;

    @ApiModelProperty(value = "是否评价")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Null(message = "非法参数")
    private Integer evaluate;

    @ApiModelProperty(value = "是否分享")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Null(message = "非法参数")
    private Integer share;
}
