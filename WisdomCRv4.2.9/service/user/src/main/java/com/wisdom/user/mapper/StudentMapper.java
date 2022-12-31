package com.wisdom.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wisdom.user.bean.dto.ScoreQueryDTO;
import com.wisdom.user.bean.dto.UserLoginDTO;
import com.wisdom.user.bean.vo.LoginVO;
import com.wisdom.user.bean.vo.StuAboutClassVO;
import com.wisdom.user.bean.vo.StudentDetailedVO;
import com.wisdom.user.domain.StuScore;
import com.wisdom.user.domain.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface StudentMapper extends BaseMapper<Student> {
    LoginVO login(UserLoginDTO userLoginDTO);
    List<StudentDetailedVO> queryStudentByClassId(Long classId);
    Long addClock(Long stuId);
    List<StuAboutClassVO> classQueryAllStu(String email);
    Long getStuStudyTime(Long id);
    List<Integer> getStuScoreType(Long stuId);
    List<StuScore> getStuScore(ScoreQueryDTO scoreQueryDTO);
    Long addScore(Long stuId);
    void updateScore(@Param("stuId") Long stuId, @Param("stuScore") int stuScore);
    StudentDetailedVO getStuByStuId(@Param("stuId") Long stuId);
    List<Map> getStuClockDate(Long stuId, String date1, String date2);
    String selectStudentName(Long stuId);
    int getStuScoreByStuId(Long stuId);
}
