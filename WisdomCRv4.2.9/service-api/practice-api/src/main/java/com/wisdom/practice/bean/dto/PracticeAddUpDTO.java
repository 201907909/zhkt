package com.wisdom.practice.bean.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * @author WisdomCR
 * @since 2022/11/3
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "PracticeAddUpDTO",description = "创建实践任务DTO对象")
public class PracticeAddUpDTO implements Serializable {
    @ApiModelProperty(value = "主键ID，前端无需提供")
    private Long id;

    @ApiModelProperty(value = "实践ID，前端无需提供")
    @Null(message = "非法数据")
    private Long praId;

    @ApiModelProperty(value = "教师ID，前端无需提供")
    private Long teaId;

    @ApiModelProperty(value = "课程ID")
    @NotNull(message = "课程ID不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long couId;

    @ApiModelProperty(value = "实践类别ID")
    @NotNull(message = "实践类别ID不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long praTypeId;

    @ApiModelProperty(value = "实践名称")
    @NotEmpty(message = "实践名称不能为空")
    private String praName;

    @ApiModelProperty(value = "实践地点")
    @NotEmpty(message = "实践地址不能为空")
    private String praSite;

    @ApiModelProperty(value = "实践内容")
    @NotEmpty(message = "实践内容不能为空")
    @Size(max=125, message="实践内容不能超过125字")
    private String praMain;

    @ApiModelProperty(value = "实践开始时间")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date createTime;

    @ApiModelProperty(value = "实践宣传图")
    private String praImg;

    @ApiModelProperty(value = "旧实践宣传图")
    private String oldImg;
}
