package com.wisdom.durations.bean.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CouCommentVO {
    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 让系统序列化时，保留相关精度
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程评论Id")
    private Long couComId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程评论Id")
    private Long couId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "学生Id")
    private Long stuId;

    @ApiModelProperty(value = "学生姓名")
    private String stuName;

    @ApiModelProperty(value = "学生image")
    private String image;

    @ApiModelProperty(value = "课程评论内容")
    private String couComment;

    @ApiModelProperty(value = "点赞数")
    private Long clicks;

    @ApiModelProperty(value = "下踩数")
    private Long drops;

    @ApiModelProperty(value = "是否点赞，0为不赞，1为赞")
    private int isClick;

    @ApiModelProperty(value = "是否下踩，0为不踩，1为踩")
    private int isDrop;

}
