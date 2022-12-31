package com.wisdom.durations.bean.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CourseProgressVO {

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程Id")
    private Long couId;

    @ApiModelProperty(value = "课程封面")
    private String couPic;

    @ApiModelProperty(value = "教师名称")
    private String teaName;

    @ApiModelProperty(value = "课程名称")
    private String couName;

    @ApiModelProperty(value = "学习进度的占比")
    private Long ratio;

}
