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
@ApiModel(value = "StuClockDetailsVO",description = "学生签到详情VO对象")
public class StuClockDetailsVO {
    @ApiModelProperty(value = "学生打卡记录")
    private List<StuClockRecordVO> stuClockRecordVOList;

    @ApiModelProperty(value = "连续打卡天数")
    private Integer continuedClock;

    @ApiModelProperty(value = "明日可得积分")
    private Integer tomorrowScore;
}
