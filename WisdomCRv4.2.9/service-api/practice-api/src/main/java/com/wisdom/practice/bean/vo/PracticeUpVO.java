package com.wisdom.practice.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;

/**
 * @author WisdomCR
 * @since 2022/11/3
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "PracticeUpVO",description = "修改实践任务VO对象")
public class PracticeUpVO implements Serializable {
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "实践ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long praId;

    @ApiModelProperty(value = "教师ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long teaId;

    @ApiModelProperty(value = "教师名称")
    private String teaName;

    @ApiModelProperty(value = "课程名称")
    private String couName;

    @ApiModelProperty(value = "实践名称")
    private String praName;

    @ApiModelProperty(value = "实践类别")
    private String praType;

    @ApiModelProperty(value = "实践地点")
    private String praSite;

    @ApiModelProperty(value = "实践内容")
    private String praMain;

    @ApiModelProperty(value = "实践宣传图")
    private String praImgName;

    @ApiModelProperty(value = "实践宣传图IO")
    private String praImgIO;

    @ApiModelProperty(value = "实践开始时间")
    private Date createTime;

    @ApiModelProperty(value = "实践结束时间")
    private Date endTime;

    @ApiModelProperty(value = "点赞数")
    private Integer goodNumber;

    @ApiModelProperty(value = "参加数")
    private Integer joinNumber;

    @ApiModelProperty(value = "提交数")
    private Integer completeNumber;
}
