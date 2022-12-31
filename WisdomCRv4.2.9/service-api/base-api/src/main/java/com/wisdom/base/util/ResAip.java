package com.wisdom.base.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResAip {

    private String conclusion;

    private Long log_id;

    private Integer conclusionType;

    private Object data;
}
