package com.wisdom.chapter.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ChapterVO {
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long couId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long chaId;

    private String chaTitle;

    private String chaUrl;

    private Long chaTime;

    private Long startTime;

    private Long progressTime;

    @ApiModelProperty(value = "学习进度的占比")
    private int ratio;

}
