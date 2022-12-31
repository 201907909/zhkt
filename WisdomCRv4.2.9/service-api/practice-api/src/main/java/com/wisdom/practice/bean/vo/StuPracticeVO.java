package com.wisdom.practice.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author WisdomCR
 * @since 2022/11/3
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "StuPracticeVO", description = "学生实践任务VO对象")
public class StuPracticeVO {
    @ApiModelProperty(value = "是否提交")
    private Integer isEnd;

    @ApiModelProperty(value = "是否删除")
    private Integer deleted;
}
