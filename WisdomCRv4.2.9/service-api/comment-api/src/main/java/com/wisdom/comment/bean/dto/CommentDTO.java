package com.wisdom.comment.bean.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "CommentDTO",description = "评论DTO对象")
@TableName(value = "CommentDTO")
public class CommentDTO {

    @ApiModelProperty(value = "评论ID")
    private Long comId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long infoId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @ApiModelProperty(value = "评论内容")
    private String comContent;

    @ApiModelProperty(value = "角色")
    private Integer role;
}
