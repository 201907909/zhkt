package com.wisdom.course.bean.vo;

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
@ApiModel(value = "CouTypeDetailedVO",description = "课程类别信息VO对象")
public class CouTypeDetailedVO implements Serializable {
    @ApiModelProperty(value = "课程类别")
    private List<Object> typeList1;

    @ApiModelProperty(value = "课程详细信息")
    private List<CourseDetailedVO> courseDetailedVOS;
}
