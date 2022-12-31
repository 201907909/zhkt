package com.wisdom.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wisdom.interlocution.bean.vo.InterlocutionVO;
import com.wisdom.interlocution.bean.vo.ReplyVO;
import com.wisdom.interlocution.domain.Interlocution;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface WenDaMapper extends BaseMapper<Interlocution> {

    List<InterlocutionVO> getIssue(Long stuId);

    List<ReplyVO> getReply(@Param("queId") Long queId, @Param("userId") Long stuId);

    List<InterlocutionVO> getMyIssues(Long stuId);

    InterlocutionVO getReplyMyIssue(Long queId);

    InterlocutionVO getMyReplyByqueId(Long queId);
}
