package com.wisdom.user.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "ScoreClassVO",description = "班级学生积分VO对象")
public class ScoreClassVO implements Serializable {
    @ApiModelProperty(value = "积分")
    private Integer value;

    @ApiModelProperty(value = "积分类别")
    private String name;
}
