package com.wisdom.base.util;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespPageBean {

    /**
     * 总条数
     */
    @ApiModelProperty("总条数")
    private Long total;
    /**
     * 数据list
     */
    @ApiModelProperty("数据list")
    private List<?> data;

    private Long pages;

    private int unReadNum;
}
