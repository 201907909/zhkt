package com.wisdom.stclass.bean.vo;

import com.wisdom.course.bean.vo.CourseBriefVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author WisdomCR
 * @since 2022-10-30
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "ClassCourseVO",description = "班级课程VO对象")
public class ClassCourseVO implements Serializable {
    @ApiModelProperty(value = "班级名称")
    private String className;

    @ApiModelProperty(value = "班级LogoIO")
    private String classLogoIO;

    @ApiModelProperty(value = "班级全部课程")
    private List<CourseBriefVO> courses;

    @ApiModelProperty(value = "全部课程")
    private List<CourseBriefVO> courseAll;

    @ApiModelProperty(value = "完整全部课程")
    private List<CourseBriefVO> courseAllComplete;

    @ApiModelProperty(value = "班级全部课程类别占比")
    private Map<String, Object>[] classCourseType;

    @ApiModelProperty(value = "全部课程类别占比")
    private Map<String, Object>[] allCourseType;
}
