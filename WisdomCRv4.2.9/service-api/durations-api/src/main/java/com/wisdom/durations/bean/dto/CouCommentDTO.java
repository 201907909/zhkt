package com.wisdom.durations.bean.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CouCommentDTO {
    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 让系统序列化时，保留相关精度
     */
    @ApiModelProperty(value = "课程评论Id")
    private Long couComId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程Id")
    private Long couId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "学生Id")
    private Long stuId;

    @ApiModelProperty(value = "课程评论内容")
    private String couComment;

}
