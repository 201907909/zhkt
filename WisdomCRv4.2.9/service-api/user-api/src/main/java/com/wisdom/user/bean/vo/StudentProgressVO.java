package com.wisdom.user.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.List;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "StudentProgressVO",description = "学生学习时长VO对象")
public class StudentProgressVO implements Serializable {
    @ApiModelProperty(value = "学生每日学习时长开始日期")
    private String stuDailyTimeStart;

    @ApiModelProperty(value = "学生每日学习时长")
    private List<List<Integer>> stuDailyTime;

    @ApiModelProperty(value = "获取学生打卡记录")
    private List<String> stuClockDate;

    @ApiModelProperty(value = "获取学生连续打卡时长")
    private Integer stuContinuedClock;
}
