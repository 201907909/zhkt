package com.wisdom.user.bean.dto;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "AuditPassDTO",description = "审核通过DTO对象")
public class AuditPassDTO {

    public List<String> id;

    public List<String> auditId;

    public int auditType;
}
