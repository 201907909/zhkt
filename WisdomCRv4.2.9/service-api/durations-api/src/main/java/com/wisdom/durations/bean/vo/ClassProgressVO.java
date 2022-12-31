package com.wisdom.durations.bean.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ClassProgressVO {

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 让系统序列化时，保留相关精度
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "班级id")
    private Long classId;

    @ApiModelProperty(value = "班级名称")
    private String className;

    @ApiModelProperty(value = "教师名称")
    private String teaName;

    @ApiModelProperty(value = "班级logo")
    private String classLogo;

    @ApiModelProperty(value = "课程进度")
    private List<CourseProgressVO> courseProgressVOList;


}
