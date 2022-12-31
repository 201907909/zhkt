package com.wisdom.course.bean.vo;

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
 * @since 2022-10-30
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "AnyFieryCourseVO",description = "各层面热度课程VO对象")
public class AnyFieryCourseVO implements Serializable {
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程ID")
    private Long couId;

    @ApiModelProperty(value = "课程名称")
    private String couName;

    @ApiModelProperty(value = "教师名称")
    private String teaName;

    @ApiModelProperty(value = "课程封面")
    private String couPic;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "课程价格")
    private Integer couPrice;       // 2

    @ApiModelProperty(value = "课程收藏数")
    private Integer couCollNum;     // 4

    @ApiModelProperty(value = "课程分享数")
    private Integer couShareNum;        // 4

    @ApiModelProperty(value = "点击量")
    private Integer clicks;     // 1

    @ApiModelProperty(value = "购买量")
    private Integer purchaseNum;        // 5

    @ApiModelProperty(value = "评价量")
    private Integer evaluateNum;        // 3

    @ApiModelProperty(value = "点赞量")
    private Integer recommendNum;       // 4

    @ApiModelProperty(value = "热度得分")
    private Integer score;
}
