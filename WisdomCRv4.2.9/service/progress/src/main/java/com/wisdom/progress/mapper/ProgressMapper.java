package com.wisdom.progress.mapper;

import com.wisdom.chapter.bean.vo.ChapterVO;
import com.wisdom.durations.bean.dto.CouCommentDTO;
import com.wisdom.durations.bean.dto.HisToryProgressVO;
import com.wisdom.durations.bean.dto.HistoryLearningDurationUPDTO;
import com.wisdom.durations.bean.dto.ProgressDTO;
import com.wisdom.durations.bean.vo.ChapterProgressVO;
import com.wisdom.durations.bean.vo.ClassProgressVO;
import com.wisdom.durations.bean.vo.CouCommentVO;
import com.wisdom.durations.bean.vo.CourseProgressVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface ProgressMapper {
    Boolean addClasCouProgress(ProgressDTO progressDTO);
    Boolean addCouProgress(ProgressDTO progressDTO);
    void updateClasCouProgress(ProgressDTO progressDTO);
    void updateCouProgress(ProgressDTO progressDTO);
    Long getClaCouStartTime(@Param("stuId") Long stuId, @Param("chaId") Long chaId, @Param("classId") Long classId);
    Long getCouStartTime(@Param("stuId") Long stuId, @Param("chaId") Long chaId);
    List<ClassProgressVO> getClasCouProgress(@Param("stuId") Long stuId);
    List<CourseProgressVO> getCouProgress(@Param("stuId") Long stuId);
    Long isRecord(@Param("chaId") Long chaId, @Param("stuId") Long stuId, @Param("classId") Long classId);
    Long isZXRecord(@Param("chaId") Long chaId, @Param("stuId") Long stuId);
    Long getClasCouProgressTime(@Param("chaId") Long chaId, @Param("stuId") Long stuId, @Param("classId") Long classId);
    Long getCouProgressTime(@Param("chaId") Long chaId, @Param("stuId") Long stuId);
    List<Long> isExist(@Param("stuId") Long stuId,  @Param("couId") Long couId);
    List<CourseProgressVO> getClassCou(@Param("classId") Long classId);
    ChapterProgressVO getNewClaChaStartTime(@Param("couId") Long couId, @Param("classId") Long classId, @Param("stuId") Long stuId);
    ChapterProgressVO getNewCouChaStartTime(@Param("couId") Long couId, @Param("stuId") Long stuId);
    List<Long> getClassCouAllTime(@Param("classId") Long classId, @Param("couId") Long couId, @Param("stuId") Long stuId);
    List<Long> getCouAllTime(@Param("couId") Long couId, @Param("stuId") Long stuId);
    List<ChapterProgressVO> claCourseChaPro(@Param("stuId") Long stuId, @Param("classId") Long classId, @Param("couId") Long couId);
    List<ChapterProgressVO> courseChaPro(@Param("stuId") Long stuId,  @Param("couId") Long couId);
    List<ChapterProgressVO> clasCourseChaTime(@Param("stuId") Long stuId, @Param("classId") Long classId, @Param("couId") Long couId);
    List<ChapterProgressVO> courseChaTime(@Param("stuId") Long stuId,  @Param("couId") Long couId);
    List<CouCommentVO> getCouCommment(@Param("couId") Long couId);
    void addComment(CouCommentDTO couCommentDTO);
    CouCommentVO getClicks(@Param("couComId") Long couComId);
    int isClick(@Param("couComId") Long couComId, @Param("stuId") Long stuId);
    int isDrop(@Param("couComId") Long couComId, @Param("stuId") Long stuId);
    void deletedClick(@Param("couComId") Long couComId, @Param("stuId") Long stuId);
    void updateCouComClicks(@Param("couComId") Long couComId, @Param("clicks") Long clicks);
    void addClick(@Param("couComId") Long couComId, @Param("stuId") Long stuId);
    void deletedDrop(@Param("couComId") Long couComId, @Param("stuId") Long stuId);
    void updateCouComDrops(@Param("couComId") Long couComId, @Param("drops") Long drops);
    void addDrop(@Param("couComId") Long couComId, @Param("stuId") Long stuId);
    Long addHisToryProgress(HisToryProgressVO hisToryProgressVO);
    Long addHistoryLearningDuration(ProgressDTO progressDTO);
    Boolean upHistoryLearningDuration(HistoryLearningDurationUPDTO historyLearningDurationUPDTO);
    void updateHisToryProgress(@Param("dailyTime") Long dailyTime, @Param("id") Long id);
    Long getYStudyTime(@Param("stuId") Long stuId, @Param("yesterday") String yesterday);
    Long monthTime(@Param("stuId") Long stuId, @Param("monthFirstDay") String monthFirstDay);
    ChapterVO chapterProgress(@Param("stuId") Long stuId, @Param("chaId") Long chaId, @Param("classId") Long classId);
    Long getHisToryProgress(@Param("id") Long id);
    void addScore(@Param("stuId") Long stuId, @Param("scoreType") int scoreType, @Param("score") int score);
}
