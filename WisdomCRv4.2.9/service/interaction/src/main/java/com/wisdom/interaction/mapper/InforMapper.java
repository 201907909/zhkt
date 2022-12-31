package com.wisdom.interaction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wisdom.information.bean.dto.InforDTO;
import com.wisdom.information.bean.vo.CommentVO;
import com.wisdom.information.bean.vo.InforVO;
import com.wisdom.information.domain.Information;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface InforMapper extends BaseMapper<Information> {
    IPage<InforVO> getAllInfor(@Param("page") IPage<InforVO> page, @Param("userId") Long userId);
    List<InforVO> getAllInfor1(@Param("start") int start, @Param("noRead") int noRead, @Param("userId") Long userId);
    Long getTotal(@Param("userId") Long userId);
    Long getAllTotal();
    IPage<InforVO> getAllInforByTeaId(@Param("page") IPage<InforVO> page, @Param("teaId") Long teaId);
    List<InforVO> A();
    IPage<InforVO> search(@Param("page") IPage<InforVO> page, @Param("val") String val);
    IPage<InforVO> teaSearch(@Param("page") IPage<InforVO> page, @Param("val") String val, @Param("teaId") Long teaId);
    void addClicks(@Param("infoId") Long infoId, @Param("userId") Long userId);
    List<InforVO> B(@Param("val") String val);
    void add(InforDTO inforDTO);
    Long getCount(@Param("infoId") Long infoId);

    int isRead(@Param("infoId") Long infoId, @Param("userId") Long userId);

    int readNum(@Param("userId") Long userId);

    InforVO inforDetail(@Param("infoId") Long infoId);
    InforVO C(@Param("infoId") Long infoId);
    IPage<CommentVO> getCommentByInfoId(@Param("page") IPage<CommentVO> page, @Param("infoId") Long infoId);
    List<CommentVO> commentListByInfoId(@Param("infoId") Long infoId);
    List<CommentVO> D(@Param("infoId") Long infoId);
    int isRecommend(@Param("infoId") Long infoId, @Param("userId") Long userId);
    int getCommenyTotal(@Param("infoId") Long infoId);
    void updateInforRecom(@Param("infoId") Long infoId, @Param("infoLike") Long infoLike);
    void updateComRecommend(@Param("infoId") Long infoId, @Param("userId") Long userId, @Param("recommend") int recommend);
    void deletedInfo(@Param("infoId") Long infoId);
    void deletedCom(@Param("infoId") Long infoId);
    void deletedClicks(@Param("infoId") Long infoId, @Param("userId") Long userId);
    void updateInfor(InforDTO inforDTO);
    void remoteUpdateInfo(InforDTO inforDTO);
    List<InforVO> teaNoticeInfo(@Param("teaId") Long teaId);
    Long getClicks(@Param("infoId") Long infoId);
    void read(@Param("infoId") Long infoId, @Param("userId") Long userId);
}
