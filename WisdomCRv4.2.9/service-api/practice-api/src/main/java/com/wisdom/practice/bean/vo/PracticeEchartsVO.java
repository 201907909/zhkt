package com.wisdom.practice.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author WisdomCR
 * @since 2022/11/3
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "PracticeEchartsVO",description = "历史实践VO对象")
public class PracticeEchartsVO implements Serializable {
    @ApiModelProperty(value = "实践结束日期")
    private Date date;

    @ApiModelProperty(value = "实践名称")
    private String name;

    @ApiModelProperty(value = "实践详情")
    private PracticeUpVO practice;

    @ApiModelProperty(value = "班级数")
    private List<PraClassVO> classNumber;

    @ApiModelProperty(value = "前段日期")
    private List<String> beforeDate;

    @ApiModelProperty(value = "后段日期")
    private List<String> afterDate;

    @ApiModelProperty(value = "前段日期数据")
    private List<Long> beforeData;

    @ApiModelProperty(value = "后段日期数据")
    private List<Long> afterData;
}
