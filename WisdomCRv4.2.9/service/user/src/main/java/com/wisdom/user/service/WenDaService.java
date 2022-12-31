package com.wisdom.user.service;

import com.wisdom.interlocution.bean.vo.InterlocutionVO;

import java.util.List;

public interface WenDaService {
    List<InterlocutionVO> getMyAllReplys(Long stuId);
    List<InterlocutionVO> getMyIssues(Long stuId);
    InterlocutionVO getReplyMyIssue(Long queId);
    InterlocutionVO getMyReplyByqueId(Long queId, Long stuId);
}
