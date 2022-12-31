package com.wisdom.chapter.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author WisdomCR
 * @since 2022/11/3
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "ChapterUpdateInfoDTO",description = "章节修改名称地址DTO对象")
public class ChapterUpdateInfoDTO implements Serializable {
    @ApiModelProperty("雪花id")
    @NotNull(message = "雪花id不能为空")
    private String id;

    @ApiModelProperty("章节名称")
    @Size(max = 20, message="章节名称不能超过20字")
    private String chaTitle;

    @ApiModelProperty("章节地址")
    @Size(max=100,message="章节地址不能超过100字")
    private String chaUrl;

    @ApiModelProperty("章节时长，单位分钟，默认0")
    private Long chaTime;
}
