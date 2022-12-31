package com.wisdom.user.service;

import com.wisdom.base.util.R;
import com.wisdom.user.bean.dto.StuUpdateDTO;
import com.wisdom.user.bean.dto.UserLoginDTO;
import com.wisdom.user.bean.vo.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author WisdomCR
 * @since 2022-10-30
 */
public interface StudentService {
    Boolean isExist(String email);
    StudentDetailedVO getStuDetails(Long id);
    LoginVO login(UserLoginDTO userLoginDTO);
    R updateInfo(StuUpdateDTO stuUpdateDTO);
    Boolean updatePassword(String password, Long id);
    Boolean forgetPassword(String email,String password);
    List<StudentDetailedVO> queryStudentByClassId(Long classId);
    StudentProgressVO queryStudentProgressByStuId(Long stuId);
    Boolean updateStuStudyTime(Long stuStudyTime, Long id);
    Boolean addClock(Long stuId);
    List<StuAboutClassVO> classQueryAllStu(String email);
    List<ScoreClassVO> classQueryStuScore(Long stuId);
    String stuFile(MultipartFile multipartFile);
    Boolean addScore(Long stuId, Integer score);// 用户打卡加分
    Boolean subScore(Long stuId, Integer score);// 用户补签减分
    StudentDetailedVO getStuByStuId(Long stuId);
    List<String> selectStudentName(List<Long> stuIds);
    int getStuScoreByStuId(Long stuId);
    void updateScore(Long stuId, int stuScore);
}
