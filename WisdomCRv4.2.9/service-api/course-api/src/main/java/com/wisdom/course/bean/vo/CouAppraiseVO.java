package com.wisdom.course.bean.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@ApiModel(value = "CouAppraiseVO",description = "课程评价VO对象")
public class CouAppraiseVO {

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "课程Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long couId;

    @ApiModelProperty(value = "学生Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long stuId;

    @ApiModelProperty(value = "学生名字")
    private String stuName;

    @ApiModelProperty(value = "学生头像")
    private String image;

    @ApiModelProperty(value = "课程评价内容")
    private String couAppraise;

    @ApiModelProperty(value = "课程评分")
    private int evScore;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;
}
