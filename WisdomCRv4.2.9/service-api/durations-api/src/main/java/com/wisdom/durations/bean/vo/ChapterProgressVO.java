package com.wisdom.durations.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ChapterProgressVO {

    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long couId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long chaId;

    private String url;

    private Long startTime;

    private Long progressTime;

}
