package com.wisdom.interlocution.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Interlocution对象",description = "问答对象")
@TableName(value = "interlocution")
public class Interlocution implements Serializable {
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "话题/问题ID")
    private Long queId;

    @ApiModelProperty(value = "发布/回复者ID")
    private Long userId;

    @ApiModelProperty(value = "被回复的问题ID")
    private Long answeredTqId;

    @ApiModelProperty(value = "是否被采纳")
    private Integer isSelect;

    @ApiModelProperty(value = "图片")
    private String img;

    @ApiModelProperty(value = "点赞数(问题)")
    private Long clicks;

    @ApiModelProperty(value = "收藏数(问题)")
    private Long favorites;

    @ApiModelProperty(value = "是否点赞(问题)")
    private Integer isClick;

    @ApiModelProperty(value = "是否收藏(问题)")
    private Integer isFavorite;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "是否为问题")
    private Integer isQuestion;

    @ApiModelProperty(value = "大类型")
    private String maxTypeId;

    @ApiModelProperty(value = "小类型")
    private String minTypeId;

    @ApiModelProperty(value = "乐观锁")
    @Version
    private Integer version;

    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private Integer deleted;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
