package com.wisdom.information.bean.vo;

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
@ApiModel(value = "InforVO对象",description = "资讯VO对象")
public class InforVO {

    private static final long serialVersionUID=1L;

    /**
     * 让系统序列化时，保留相关精度
     */
    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "资讯ID")
    private Long infoId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "教师ID")
    private Long teaId;

    @ApiModelProperty(value = "资讯标题")
    private String infoTitle;

    @ApiModelProperty(value = "资讯内容")
    private String infoMain;

    @ApiModelProperty(value = "资讯作者")
    private String infoAuthor;

    @ApiModelProperty(value = "点赞数")
    private Long infoLike;

    @ApiModelProperty(value = "头像")
    private String image;

    @ApiModelProperty(value = "资讯图片")
    private String infoImage;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "评论数量")
    private Long count;

    @ApiModelProperty(value = "是否点赞；默认：0，已点赞：1")
    private int recommend;

//    @ApiModelProperty(value = "评论分页")
//    private IPage<CommentVO> commentVOs;

    @ApiModelProperty(value = "评论分页")
    private List<CommentVO> commentVOs;

    @ApiModelProperty(value = "是否已读，0为为未读，1为已读")
    private int isRead;
}
