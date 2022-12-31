package com.wisdom.stclass.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author WisdomCR
 * @since 2022-10-30
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "ClassAddDTO",description = "添加班级DTO对象")
public class ClassAddDTO implements Serializable {
    @ApiModelProperty(value = "班级ID，前端无需提供")
    @Null(message = "非法参数")
    private Long classId;

    @ApiModelProperty(value = "教师ID，前端无需提供")
    @Null(message = "非法参数")
    private Long teaId;

    @ApiModelProperty(value = "班级名称")
    @NotEmpty(message = "班级名称不能为空")
    private String className;

    @ApiModelProperty(value = "班级Logo")
    private String classLogo;

    @ApiModelProperty(value = "班级详情")
    @Size(max=200,message="班级详情不能超过200字")
    private String classDetails;
}
