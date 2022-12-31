package com.wisdom.user.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.wisdom.course.bean.vo.CourseAboutVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.List;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "StudentDetailedVO",description = "学生详情VO对象")
public class StudentDetailedVO implements Serializable {
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "学生ID")
    private Long stuId;

    @ApiModelProperty(value = "学生姓名")
    private String stuName;

    @ApiModelProperty(value = "性别")
    private int gender;

    @ApiModelProperty(value = "头像IO")
    private String imageIO;

    @ApiModelProperty(value = "头像")
    private String image;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "学生学习时长")
    private Long stuStudyTime;

    @ApiModelProperty(value = "学生总积分")
    private Integer scoreTotal;

    @ApiModelProperty(value = "学生积分")
    private List<ScoreClassVO> stuScore;

    @ApiModelProperty(value = "学生每日学习时长开始日期")
    private String stuDailyTimeStart;

    @ApiModelProperty(value = "学生每日学习时长")
    private List<List<Integer>> stuDailyTime;

    @ApiModelProperty(value = "获取学生打卡记录")
    private List<String> stuClockDate;

    @ApiModelProperty(value = "获取学生连续打卡时长")
    private Integer stuContinuedClock;
}
