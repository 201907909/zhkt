package com.wisdom.stclass.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.wisdom.course.bean.vo.CourseAboutVO;
import com.wisdom.user.bean.vo.StudentDetailedVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.List;

/**
 * @author WisdomCR
 * @since 2022-10-30
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "ClassDetailedVO",description = "班级详情VO对象")
public class ClassDetailedVO implements Serializable {
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "班级ID")
    private Long claId;

    @ApiModelProperty(value = "班级名称")
    private String className;

    @ApiModelProperty(value = "班级LogoIO")
    private String classLogoIO;

    @ApiModelProperty(value = "班级Logo")
    private String classLogo;

    @ApiModelProperty(value = "班级详情")
    private String classDetails;

    @ApiModelProperty(value = "班级人数")
    private Integer classStuNum;

    @ApiModelProperty(value = "班级全部学生")
    private List<StudentDetailedVO> students;

    @ApiModelProperty(value = "班级全部课程")
    private List<CourseAboutVO> courses;
}
