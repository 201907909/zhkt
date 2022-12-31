package com.wisdom.chapter.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author WisdomCR
 * @since 2022/11/3
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "ChapterOfFatherVO",description = "父章节VO对象")
public class ChapterInfoVO implements Serializable {
    //@JsonSerialize(using = ToStringSerializer.class)// 防止前端丢失精度
    @ApiModelProperty(value = "雪花id")
    private Long id;

    @ApiModelProperty(value = "章节id")
    private String chaId;

    @ApiModelProperty(value = "课程id")
    private String couId;

    @ApiModelProperty(value = "章节名称")
    private String chaTitle;

    @ApiModelProperty(value = "章节排列序号")
    private Integer chaIndex;

    @ApiModelProperty(value = "章节地址")
    private String chaUrl;

    @ApiModelProperty(value = "章节时长")
    private Long chaTime;
}
