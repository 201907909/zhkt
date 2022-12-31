package com.wisdom.user.mapper;

import com.wisdom.user.bean.vo.NoticeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface NoticeMapper {
    NoticeVO stuNotice(Long stuId);
    void deleted(@Param("nId") Long nId);
    void appliAuditText(@Param("noticeId") Long noticeId);
    void appliAuditImg(@Param("noticeId") Long noticeId);
    void addNotice(@Param("userId") Long userId, @Param("noticeId") Long noticeId);
    void updateNoticeState(@Param("noticeId") Long noticeId,  @Param("isPass") int isPass, @Param("deleted") int deleted);
    NoticeVO isExist(@Param("noticeId") Long noticeId);
    void updateNotice(@Param("noticeId") Long noticeId, @Param("isPass") int isPass);
    int isPass(@Param("noticeId") Long noticeId);
}
