package com.wisdom.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wisdom.course.bean.vo.CourseAddVO;
import com.wisdom.practice.bean.dto.StuPracticeDTO;
import com.wisdom.practice.bean.vo.PracticeUpVO;
import com.wisdom.practice.bean.vo.SimplePracticeVO;
import com.wisdom.practice.bean.vo.StuPracticeVO;
import com.wisdom.practice.domain.Practice;
import com.wisdom.type.bean.vo.TypeVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface PracticeMapper extends BaseMapper<Practice> {
    String getPraImgById(Long id);
    String getPraCouTypeByPraTypeId(Long praTypeId);
    Integer getPraJoinNumberByPraId(Long praId);
    Integer getPraCompleteNumberByPraId(Long praId);
    List<PracticeUpVO> getAllPracticeInfoByTea();
    List<TypeVO> getAllPracticeType();
    List<CourseAddVO> getAllCourseByTeaId(Long teaId);
    List<PracticeUpVO> getPracticeByInfo(String info);
    List<PracticeUpVO> getOverduePractice();
    List<String> getPraClass(Long praId);
    List<Map> getBeforeAndAfterDateData(Long praId);
    int addPracticeDate(Long praId);
    int deletedPracticeDate(Long praId);
    Integer getPracticeIsEnd(Long id);
    Long getPracticeId(Long id);
    StuPracticeVO getStuPractice(StuPracticeDTO stuPracticeDTO);
    int joinPractice(StuPracticeDTO stuPracticeDTO);
    int upJoinPractice(StuPracticeDTO stuPracticeDTO);
    int upSubmitPractice(StuPracticeDTO stuPracticeDTO);
    Integer getPracticeDateVersion(StuPracticeDTO stuPracticeDTO);
    Integer getPracticeVersion(Long praId);
    int upPracticeDateActivity(StuPracticeDTO stuPracticeDTO);
    int upPracticeGoodNum(StuPracticeDTO stuPracticeDTO);
    List<SimplePracticeVO> getAllPracticeSimpleInfo(Long couId);
    SimplePracticeVO getPracticeSimpleInfo(Long id);
    Integer getStuPracticeState(Long id, Long stuId);
    Integer getStuPracticeIsEnd(Long id, Long stuId);
    List<SimplePracticeVO> getPracticeSimpleByInfo(String info, Long couId);
}
