package com.wisdom.interaction.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wisdom.base.util.R;
import com.wisdom.base.util.RespPageBean;
import com.wisdom.interlocution.bean.dto.InterlocutionDTO;
import com.wisdom.interlocution.bean.dto.ReplyDTO;
import com.wisdom.interlocution.bean.vo.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WenDaService {
    R add(InterlocutionDTO interlocutionDTO);
    List<MaxTypeVO> getAllType();
    R reply(ReplyDTO replyDTO);
    RespPageBean getAllWenDa(IPage<IssueVO> page);
    RespPageBean teaGetAllWenDa(IPage<IssueVO> page, Long teaId);
    Long getAllTotal();
    IssueVO issueVODetail(Long queId, Long userId);
    IssueVO replyVOSDetail(Long queId, IPage<ReplyVO> page);
    IssueVO teaIssueVODetail(Long queId, Long userId);
    IssueVO issueVODetailList(Long queId, Long userId);
    ClickVO click(Long queId, Long userId);
    ClickVO cancelClick(Long queId, Long userId);
    void adopt(Long replyId, Long userId, Long queId);
    void cancelAdopt(Long replyId, Long userId);
    RespPageBean search(String val, IPage<IssueVO> page);
    String wenDaFile(MultipartFile multipartFile);
    void deletedWenDa(List<Long> qId);
    void remoteUpdateWenDa(InterlocutionDTO interlocutionDTO);
    List<IssueVO> stuNoticeWenDa(Long stuId);
    List<InterlocutionVO> getMyAllReplys(Long stuId);
    List<InterlocutionVO> getMyIssues(Long stuId);
    InterlocutionVO getReplyMyIssue(Long queId);
    InterlocutionVO getMyReplyByqueId(Long queId, Long stuId);
}
