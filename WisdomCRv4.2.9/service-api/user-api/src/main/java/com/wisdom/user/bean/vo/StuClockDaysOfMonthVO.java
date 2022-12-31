package com.wisdom.user.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author WisdomCR
 * @since 2022/11/12
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "StuClockDaysOfMonthVO",description = "学生本月签到天数VO对象")
public class StuClockDaysOfMonthVO {
    @ApiModelProperty(value = "学生ID")
    private List<String> yesOrNo;
}
