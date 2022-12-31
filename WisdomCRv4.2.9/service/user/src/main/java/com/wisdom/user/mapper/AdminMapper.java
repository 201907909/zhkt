package com.wisdom.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wisdom.user.bean.dto.AdminLoginDTO;
import com.wisdom.user.bean.dto.AuditImgDTO;
import com.wisdom.user.bean.dto.AuditTextDTO;
import com.wisdom.user.bean.vo.AdminLoginVO;
import com.wisdom.user.bean.vo.AuditImgVO;
import com.wisdom.user.bean.vo.AuditTextVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMapper {

    AdminLoginVO login(AdminLoginDTO adminLoginDTO);

    void addAuditText(AuditTextDTO auditTextDTO);

    void addAuditImg(AuditImgDTO auditImgDTO);

    void updateAuditText(@Param("id") Long id, @Param("auditResult") int auditResult, @Param("auditStatue") int auditStatue);

    void updateAuditImg(@Param("id") Long id, @Param("auditResult") int auditResult, @Param("auditStatue") int auditStatue);

    List<AuditTextVO> getAuditText(@Param("auditId") Long auditId);

    List<AuditImgVO> getAuditImg(@Param("auditId") Long auditId);

    IPage<AuditTextVO> reviewText(@Param("page") IPage<AuditTextVO> page);

    IPage<AuditTextVO> textSecurity(@Param("page") IPage<AuditTextVO> page, @Param("val") String val);

    IPage<AuditImgVO> imgSecurity(@Param("page") IPage<AuditImgVO> page);

    IPage<AuditTextVO> historyText(@Param("page") IPage<AuditTextVO> page);

    IPage<AuditImgVO> reviewImg(@Param("page") IPage<AuditImgVO> page);

    IPage<AuditImgVO> historyImg(@Param("page") IPage<AuditImgVO> page);

    void updateNoticeStatue(@Param("isPass") int isPass, @Param("auditId") Long auditId);
}
