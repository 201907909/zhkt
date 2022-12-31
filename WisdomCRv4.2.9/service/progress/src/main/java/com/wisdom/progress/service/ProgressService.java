package com.wisdom.progress.service;

import com.wisdom.base.util.R;
import com.wisdom.chapter.bean.vo.ChapterVO;
import com.wisdom.durations.bean.dto.CouCommentDTO;
import com.wisdom.durations.bean.dto.ProgressDTO;
import com.wisdom.durations.bean.vo.*;

import java.util.List;

public interface ProgressService {
//    ProgressDTO addClasCouProgress(ProgressDTO progressDTO);
    void addCouProgress(ProgressDTO progressDTO);
//    void updateClasCouProgress(ProgressDTO progressDTO);
//    void updateCouProgress(ProgressDTO progressDTO);
    Boolean addHistoryLearningDuration(ProgressDTO progressDTO);
    void upHistoryLearningDuration(ProgressDTO progressDTO);
    List<ClassProgressVO> getClasCouProgress(Long stuId);
    CouProgressVO claCourseDetail(Long stuId, Long classId, Long couId);
    List<CourseProgressVO> getCouProgress(Long stuId);
    CouProgressVO courseDetail(Long stuId, Long couId);
    List<CouCommentVO> getCouCommment(Long couId, Long stuId);
    R addComment(CouCommentDTO couCommentDTO);
    void click(Long couComId, Long stuId);
    void drop(Long couComId, Long stuId);
    String yStudyTime(Long stuId);
    String tStudyTime(String stuId);
    String monthTime(Long stuId);
    ChapterVO chapterProgress(Long stuId, Long chaId, Long classId);
    ChapterProgressVO newChapterProgress(Long stuId, Long couId, Long classId);
    void addScore(Long stuId, int scoreType);
}
