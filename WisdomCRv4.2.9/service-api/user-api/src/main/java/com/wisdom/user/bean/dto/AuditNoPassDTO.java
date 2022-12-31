package com.wisdom.user.bean.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;


@Data
@Accessors(chain = true)
@ApiModel(value = "AuditNoPassDTO",description = "审核不通过DTO对象")
public class AuditNoPassDTO {

    public List<String> id;

    public List<String> auditId;

    public List<String> userId;

    public int auditType;

    public List<Integer> auditSource;
}
