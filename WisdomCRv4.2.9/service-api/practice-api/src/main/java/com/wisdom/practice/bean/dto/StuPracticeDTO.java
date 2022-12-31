package com.wisdom.practice.bean.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;

/**
 * @author WisdomCR
 * @since 2022/11/3
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "StuPracticeDTO",description = "学生实践DTO对象")
public class StuPracticeDTO implements Serializable {
    @ApiModelProperty(value = "学生ID，前端无需提供")
    @Null(message = "非法参数")
    private Long stuId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "实践ID")
    @NotNull(message = "实践ID不能为空")
    private Long praId;

    @ApiModelProperty(value = "状态，前端无需提供")
    @Null(message = "非法参数")
    private Integer state;

    @ApiModelProperty(value = "评分")
    private Integer score;

    @ApiModelProperty(value = "活跃度，前端无需提供")
    @Null(message = "非法参数")
    private Long activity;

    @ApiModelProperty(value = "实践日历表乐观锁，前端无需提供")
    @Null(message = "非法参数")
    private Integer praDateVersion;

    @ApiModelProperty(value = "实践表乐观锁，前端无需提供")
    @Null(message = "非法参数")
    private Integer praVersion;

    @ApiModelProperty(value = "当天日期，前端无需提供")
    @JsonFormat(pattern="yyyy-MM-dd")
    @Null(message = "非法参数")
    private String createTime;
}
