package com.wisdom.user.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;

/**
 * @author WisdomCR
 * @since 2022/11/13
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "GetStuLearningProDTO",description = "获取学生在班级学习进度DTO对象")
public class GetStuLearningProDTO implements Serializable {
    @ApiModelProperty("主键ID，前端无需提供")
    @Null(message = "主键ID不能为空")
    private Long id;

    @ApiModelProperty("学生ID")
    @Null(message = "前端无需提供，后端获取")
    private Long stuId;

    @ApiModelProperty("课程ID")
    @NotNull(message = "课程ID不能为空")
    private Long couId;

    @ApiModelProperty("班级ID")
    @Null(message = "前端无需提供，后端获取")
    private Long classId;

    @ApiModelProperty("章节ID")
    @NotNull(message = "章节ID不能为空")
    private Long chaId;

    @ApiModelProperty("父章节ID，可为空")
    @Null(message = "前端无需提供")
    private Long faChaId;

    @ApiModelProperty("最新观看时长,单位：秒")
    @Null(message = "前端无需提供")
    private Long startTime;

    @ApiModelProperty("最长观看时长,单位：秒")
    @Null(message = "前端无需提供")
    private Long endTime;
}
