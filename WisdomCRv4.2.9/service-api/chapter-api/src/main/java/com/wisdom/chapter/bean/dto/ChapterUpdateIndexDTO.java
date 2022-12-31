package com.wisdom.chapter.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author WisdomCR
 * @since 2022/11/3
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "ChapterUpdateIndexDTO",description = "修改排列顺序DTO对象")
public class ChapterUpdateIndexDTO implements Serializable {
    @ApiModelProperty("课程ID")
    @NotNull(message = "课程ID不能为空")
    private Long couId;

    @ApiModelProperty(value = "章节ID")
    @NotNull(message = "章节ID不能为空")
    private Long chaId;

    @ApiModelProperty(value = "章节排列序号")
    @NotNull(message = "章节排列序号不能为空")
    private Integer chaIndex;
}
