package com.wisdom.base.util;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * Entity基类
 * @author WisdomCR
 * @since 2022-05-10
 */
@Data
@NoArgsConstructor
@ApiModel(value = "Entity基类",description = "Entity基类")
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
}
