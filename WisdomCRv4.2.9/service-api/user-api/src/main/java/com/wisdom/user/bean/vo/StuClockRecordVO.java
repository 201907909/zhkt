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
@ApiModel(value = "StuClockRecordVO",description = "学生签到记录VO对象")
public class StuClockRecordVO {
    @ApiModelProperty(value = "学生ID")
    private String date;

    @ApiModelProperty(value = "打卡详情")
    private String info;
}
