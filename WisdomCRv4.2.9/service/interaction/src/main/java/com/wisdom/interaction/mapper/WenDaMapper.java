package com.wisdom.interaction.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wisdom.interlocution.bean.dto.InterlocutionDTO;
import com.wisdom.interlocution.bean.dto.ReplyDTO;
import com.wisdom.interlocution.bean.vo.InterlocutionVO;
import com.wisdom.interlocution.bean.vo.IssueVO;
import com.wisdom.interlocution.bean.vo.MaxTypeVO;
import com.wisdom.interlocution.bean.vo.ReplyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WenDaMapper {

    void add(InterlocutionDTO interlocutionDTO);

    List<MaxTypeVO> getAllType();

    void reply(ReplyDTO replyDTO);

    IPage<IssueVO> getAllWenDa(@Param("page") IPage<IssueVO> page);

    List<IssueVO> getAllWenDaList();

    Long getCount(@Param("queId") Long queId);

    String getTypeName(Long couTypeId);

    IssueVO issueVODetail(@Param("queId") Long queId);

    IPage<ReplyVO> getReplyByInfoId(@Param("page") IPage<ReplyVO> page, @Param("queId") Long queId);

    List<ReplyVO> teaGetReplyByInfoId(@Param("queId") Long queId);

    List<ReplyVO> getReplyByInfoIdList(@Param("queId") Long queId);

    Integer isClick(@Param("queId") Long queId, @Param("userId") Long userId);

    void addClick(@Param("queId") Long queId, @Param("userId") Long userId);

    Long getClicks(@Param("queId") Long queId);
    void addScore(@Param("stuId") Long stuId, @Param("scoreType") int scoreType, @Param("score") int score);


    void updateIssueClicks(@Param("queId") Long queId, @Param("clicks") Long clicks);

    void deletedClick(@Param("queId") Long queId, @Param("userId") Long userId);

    Integer isAdopt(@Param("replyId") Long replyId, @Param("userId") Long userId);

    void addAdopt(@Param("replyId") Long replyId, @Param("userId") Long userId);

    void deletedAdopt(@Param("replyId") Long replyId, @Param("userId") Long userId);

    void updateComAdopt(@Param("replyId") Long replyId, @Param("isSelect") int isSelect);
    Long getUserIdByReplyId(@Param("replyId") Long replyId);
    void updateAllComAdopt(@Param("queId") Long queId);

    void deletedAllAdopt(@Param("queId") Long queId);

    IPage<IssueVO> search(@Param("val") String val, @Param("page") IPage<IssueVO> page);

    List<Long> getClaAllStuByTeaId(@Param("teaId") Long teaId);

    Long getAllTotal();

    void remoteUpdateWenDa(InterlocutionDTO interlocutionDTO);

    void deletedWenDa(@Param("qId") List<Long> qId);

    void deletedReply(@Param("qId") List<Long> qId);

    List<IssueVO> stuNoticeWenDa(@Param("stuId") Long stuId);

    //------------------------------------------------------------------------------------------------

    List<InterlocutionVO> getIssue(Long stuId);

    List<ReplyVO> getReply(@Param("queId") Long queId, @Param("userId") Long stuId);

    List<InterlocutionVO> getMyIssues(Long stuId);

    InterlocutionVO getReplyMyIssue(Long queId);

    InterlocutionVO getMyReplyByqueId(Long queId);

}
