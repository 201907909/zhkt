package com.wisdom.chapter.bean.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "ChapVO",description = "章节信息VO对象")
public class ChapVO {

    private Long id;

    private Long couId;

    private Long chaId;

    private String chaTitle;

    private Long chaTime;

    private String time;

}
