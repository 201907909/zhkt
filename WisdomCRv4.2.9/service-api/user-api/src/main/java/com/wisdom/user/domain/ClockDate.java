package com.wisdom.user.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "ClockDate",description = "学生对象")
@TableName(value = "clock_date")
public class ClockDate implements Serializable {
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "学生ID")
    private Long stuId;

    @ApiModelProperty(value = "积分详情")
    private String info;

    @ApiModelProperty(value = "创建时间")
    private String createTime;
}
