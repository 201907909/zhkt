package com.wisdom.course.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "CouAVO",description = "课程评价VO对象")
public class CouAVO {

    @ApiModelProperty(value = "综合评价分数")
    private Float allScore;

    @ApiModelProperty(value = "评价人数")
    private Long num;

    @ApiModelProperty(value = "评价内容的集合")
    private List<CouAppraiseVO> couAppraiseVOList;
}
