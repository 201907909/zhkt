package com.wisdom.stclass.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author WisdomCR
 * @since 2022-10-30
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "DailyTimeVO",description = "学习时长VO对象")
public class DailyTimeVO {
    @ApiModelProperty(value = "学习时长")
    private Long dailyTime;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
