package com.wisdom.durations.bean.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class HisToryProgressVO {

    private Long id;

    private Long stuId;

    private Long couId;

    private Long dailyTime;
}
