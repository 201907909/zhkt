package com.wisdom.interlocution.bean.vo;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "IssueVO对象",description = "问题对象")
public class IssueVO {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "话题/问题ID")
    private Long queId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "发布/回复者ID")
    private Long userId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "被回复的问题ID")
    private Long answeredTqId;

    @ApiModelProperty(value = "是否被采纳")
    private Integer isSelect;

    @ApiModelProperty(value = "图片")
    private String img;

    @ApiModelProperty(value = "图片数组")
    private List<String> imgs;

    @ApiModelProperty(value = "头像")
    private String image;

    @ApiModelProperty(value = "点赞数(问题)")
    private Long clicks;

    @ApiModelProperty(value = "收藏数(问题)")
    private Long favorites;

    @ApiModelProperty(value = "是否点赞(问题)")
    private Integer isClick;

    @ApiModelProperty(value = "是否收藏(问题)")
    private Integer isFavorite;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "标题")
    private String title;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "大类")
    private Long maxTypeId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "小类")
    private Long minTypeId;

    @ApiModelProperty(value = "大类名字")
    private String maxName;

    @ApiModelProperty(value = "小类名字")
    private String minName;

    @ApiModelProperty(value = "评论数量")
    private Long count;

    @ApiModelProperty(value = "发布者")
    private String userName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "是否为发布者，0为不是，1是发布者")
    private int isPublish;

    @ApiModelProperty(value = "是否为本班学生，0为不是，1为是")
    private int isMyStu;

    @ApiModelProperty(value = "回答对应的问题")
    private List<ReplyVO> replyVOs;
//    private IPage<ReplyVO> replyVOs;
}
