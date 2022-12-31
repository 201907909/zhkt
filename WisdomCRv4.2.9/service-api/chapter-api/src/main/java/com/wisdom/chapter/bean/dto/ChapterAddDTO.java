package com.wisdom.chapter.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author WisdomCR
 * @since 2022/11/2
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "ChapterAddDTO",description = "添加章节DTO对象")
public class ChapterAddDTO implements Serializable {
    @ApiModelProperty("课程ID")
    @NotNull(message = "课程ID不能为空")
    private Long couId;

    @ApiModelProperty(value = "章节ID，前端不需要传值，后端生成雪花")
    @Null(message = "非法参数")
    private Long chaId;

    @ApiModelProperty(value = "父章节ID，可为空")
    private Long faChaId;

    @ApiModelProperty(value = "章节排列序号")
    @NotNull(message = "章节排列序号不能为空")
    private Integer chaIndex;

    @ApiModelProperty(value = "章节名称")
    @NotNull(message = "章节名称不能为空")
    @Size(max=20,message="章节名称不能超过20字")
    private String chaTitle;

    @ApiModelProperty(value = "章节地址")
    @NotNull(message = "章节地址不能为空")
    @Size(max=100,message="章节地址不能超过100字")
    private String chaUrl;

    @ApiModelProperty(value = "章节时长，前端不需要传值，后端生成通过url时长")
    @Null(message = "非法参数")
    private Long chaTime;
}
