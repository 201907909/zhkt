package com.wisdom.practice.domain;

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
@ApiModel(value = "Practice对象",description = "实践对象")
@TableName(value = "practice")
public class Practice implements Serializable {
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "实践ID")
    private Long praId;

    @ApiModelProperty(value = "教师ID")
    private Long teaId;

    @ApiModelProperty(value = "课程ID")
    private Long couId;

    @ApiModelProperty(value = "实践类别ID")
    private Long praTypeId;

    @ApiModelProperty(value = "实践名称")
    private String praName;

    @ApiModelProperty(value = "实践地点")
    private String praSite;

    @ApiModelProperty(value = "实践内容")
    private String praMain;

    @ApiModelProperty(value = "实践图片")
    private String praImg;

    @ApiModelProperty(value = "点赞数")
    private Integer goodNumber;

    @ApiModelProperty(value = "是否结束")
    private Integer isEnd;

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
