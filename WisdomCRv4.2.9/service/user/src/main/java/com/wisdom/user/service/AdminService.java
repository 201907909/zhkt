package com.wisdom.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wisdom.base.util.RespPageBean;
import com.wisdom.user.bean.dto.AdminLoginDTO;
import com.wisdom.user.bean.dto.AuditImgDTO;
import com.wisdom.user.bean.dto.AuditNoPassDTO;
import com.wisdom.user.bean.dto.AuditTextDTO;
import com.wisdom.user.bean.vo.AdminLoginVO;
import com.wisdom.user.bean.vo.AuditImgVO;
import com.wisdom.user.bean.vo.AuditTextVO;

import java.util.List;

public interface AdminService {
    AdminLoginVO login(AdminLoginDTO adminLoginDTO);

    RespPageBean reviewText(IPage<AuditTextVO> page);

    RespPageBean textSecurity(IPage<AuditTextVO> page, String val);

    RespPageBean imgSecurity(IPage<AuditImgVO> page);

    RespPageBean historyText(IPage<AuditTextVO> page);

    RespPageBean reviewImg(IPage<AuditImgVO> page);

    RespPageBean historyImg(IPage<AuditImgVO> page);

    void addAuditText(AuditTextDTO auditTextDTO);

    void overruled(Long id, Long auditId, int auditType);

    void auditPass(List<String> id, List<String> auditId, int auditType);

    void eliminate(AuditNoPassDTO auditNoPassDTO);

    void addAuditImg(AuditImgDTO auditImgDTO);

}
