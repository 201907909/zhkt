package com.wisdom.chapter.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author WisdomCR
 * @since 2022/11/8
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "ChapterDeleteDTO",description = "教师删除章节DTO对象")
public class ChapterDeleteDTO implements Serializable {
    @ApiModelProperty("课程ID")
    @NotNull(message = "课程ID不能为空")
    private Long couId;

    @ApiModelProperty(value = "章节ID")
    @NotNull(message = "章节ID不能为空")
    private Long chaId;
}
