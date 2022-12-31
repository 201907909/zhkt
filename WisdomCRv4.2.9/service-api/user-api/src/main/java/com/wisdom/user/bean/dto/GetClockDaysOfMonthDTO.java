package com.wisdom.user.bean.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * @author WisdomCR
 * @since 2022/11/13
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "GetClockDaysOfMonthDTO",description = "获取学生某月签到数DTO对象")
public class GetClockDaysOfMonthDTO implements Serializable {
    @ApiModelProperty("学生ID，前端无需提供")
    private Long stuId;

    @ApiModelProperty("明日可得积分，前端无需提供")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer tomorrowScore;

    @ApiModelProperty("年月日，获取本月签到情况参数格式:2022-11，补签参数格式:2022-11-11")
    private String yearDay;
}
