package com.wisdom.durations.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProgressDTO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "学生id")
    private Long stuId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程id")
    private Long couId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "班级Id")
    private Long classId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "章节")
    private Long chaId;

    @ApiModelProperty(value = "结束时间")
    private Long endTime;

    @ApiModelProperty(value = "学习时长")
    private Long time;

    @ApiModelProperty(value = "是否离开当前课程，0未离开，1已离开")
    private int isLeave;


}
