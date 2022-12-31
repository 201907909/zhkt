package com.wisdom.chapter.domain;

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
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Chapter",description = "章节对象")
@TableName(value = "chapter")
public class Chapter implements Serializable {
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "章或节ID")
    private Long chaId;

    @ApiModelProperty(value = "课程ID")
    private Long couId;

    @ApiModelProperty(value = "父章ID")
    private Long faChaId;

    @ApiModelProperty(value = "章或节排列序号")
    private Integer chaIndex;

    @ApiModelProperty(value = "章或节名称")
    private String chaTitle;

    @ApiModelProperty(value = "节地址")
    private String chaUrl;

    @ApiModelProperty(value = "章或节时长")
    private Long chaTime;

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
