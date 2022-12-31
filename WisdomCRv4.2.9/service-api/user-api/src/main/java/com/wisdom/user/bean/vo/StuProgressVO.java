package com.wisdom.user.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Null;
import java.io.Serializable;

/**
 * @author WisdomCR
 * @since 2022/11/13
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "StuProgressVO",description = "学生学习进度VO对象")
public class StuProgressVO implements Serializable {
    @ApiModelProperty(value = "学生学习进度")
    private Long endTime;
}
