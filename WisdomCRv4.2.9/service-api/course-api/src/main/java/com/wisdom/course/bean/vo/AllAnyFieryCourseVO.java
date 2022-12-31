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
@ApiModel(value = "AllAnyFieryCourseVO",description = "所有各层面热度课程VO对象")
public class AllAnyFieryCourseVO implements Serializable {
    @ApiModelProperty(value = "轮播图课程List")
    private List<SimpleAnyFieryCourseVO> carouselCourseList;

    @ApiModelProperty(value = "第一热度课程List")
    private List<SimpleAnyFieryCourseVO> firstCourseList;

    @ApiModelProperty(value = "第二热度课程List")
    private List<SimpleAnyFieryCourseVO> secondCourseList;

    @ApiModelProperty(value = "第三热度课程List")
    private List<List<SimpleAnyFieryCourseVO>> thirdCourseList;
}
