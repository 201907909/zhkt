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

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "commentVO",description = "评论对象")
public class CommentVO {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "评论Id")
    private Long comId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "资讯Id")
    private Long infoId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "用户Id")
    private Long userId;

    @ApiModelProperty(value = "评论内容")
    private String comContent;

    @ApiModelProperty(value = "角色（0：教师，1：学生）")
    private Integer role;

    @ApiModelProperty(value = "评论者")
    private String userName;

    @ApiModelProperty(value = "头像")
    private String image;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;
}
