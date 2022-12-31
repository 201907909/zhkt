package com.wisdom.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wisdom.chapter.bean.vo.ChapVO;
import com.wisdom.chapter.bean.vo.ChapterVO;
import com.wisdom.course.bean.dto.*;
import com.wisdom.course.bean.vo.*;
import com.wisdom.course.domain.Course;
import com.wisdom.type.bean.vo.TypeVO;
import com.wisdom.type.domain.Type;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

/**
 * @author WisdomCR
 * @since 2022-10-30
 */
@Mapper
@Repository
public interface CourseMapper extends BaseMapper<Course> {
    List<CourseBriefVO> selectCourseByClassId(Long classId);
    List<Long> selectCourseIdByClassId(Long classId);
    List<CourseBriefVO> selectAllCourse(Integer startIndex, Integer pageSize);
    List<CourseBriefVO> selectAllCourseComplete();
    List<CourseAboutVO> queryCourseByClassId(Long classId);
    List<CourseVO> getCourseByCouId(@Param("couId") List<Long> couId);
    CourseVO courseDetailByCouId(Long couId);
    List<Integer> getCourPriceByCouId(@Param("couId") List<Long> couId);
    List<Long> getChaIdsByCouId(Long couId);
    List<CourseAboutVO> getCourseByStuId(Long stuId);
    List<CouAboutClassVO> classQueryAllCourse();
    Long addCourseAndType(CouAndTypeDTO couAndTypeDTO);
    List<CourseDetailedVO> teaQueryAllCourse(Long teaId);
    List<Type> getAllType();
    Boolean upCourseAndType(CouUpTypeDTO couUpTypeDTO);
    Integer getVersion(Long id);
    Boolean addCourseInteraction(Long couId, Long stuId);
    CourseInteractionVO getCourseInteraction(Long couId, Long stuId);
    Boolean updateCouInteraction(CouInteractionUpDTO couInteractionUpDTO);
    CouAllNumVO getCouAllNum(Long couId);
    List<Long> getStuCouProgress(CouProgressDTO couProgressDTO);
    List<Long> getStuClassCouProgress(CouProgressDTO couProgressDTO);
    List<Long> getCouProgress(Long couId);
    String getCouFirstChaURL(Long couId);
    Integer getCouPrice(@Param("couId") Long couId);
    String getCouIntroduction(@Param("couId") Long couId);
    List<Long> collectionType(@Param("stuId") Long stuId);
    List<Long> preType(@Param("stuId") Long stuId);
    List<Long> clasCouType(@Param("stuId") Long stuId);
    List<Long> couType(@Param("stuId") Long stuId);
    List<CourseVO> recommendCourse(@Param("couTypeId") List<Long> couTypeId);
    List<CourseVO> maxClickCourse(@Param("num") int num);
    List<TypeVO> recommendType(@Param("couTypeId") List<Long> couTypeId);
    List<TypeVO> maxClickType(@Param("num") int num);
    String selectCourseName(Long couId);
    List<AnyFieryCourseVO> getAllAnyFieryCourse();
    List<TypeVO> getParentTypeId();
    List<TypeVO> getCouTypeByParentTypeId(Long parentTypeId);
    List<Map> getFieryCouTypeId();
    List<SimpleAnyFieryCourseVO> getCourseByInfo(String courseInfo);
    CourseDetailedVO courseDetail(@Param("couId") Long couId);
    List<CouVO> getRelatedCourse(@Param("couId") Long couId);
    List<ChapVO> allChapterByCouId(@Param("couId") Long couId);
    IPage<CouVO> search(@Param("val") String val, @Param("page") IPage<CouVO> page );
    List<ChapterVO> getClaCourseAllCha(@Param("couId") Long couId);
    void addClick(@Param("stuId") Long stuId, @Param("couId") Long couId);
    void updateCouClick(@Param("couId") Long couId, @Param("recommendNum") Long recommendNum);
    Integer isClick(@Param("stuId") Long stuId, @Param("couId") Long couId);
    Long getClicks(@Param("couId") Long couId);
    void deletedClick(@Param("stuId") Long stuId, @Param("couId") Long couId);
    IPage<CouVO> seeMore(@Param("page") IPage<CouVO> pagem, @Param("maxTypeId") Long maxTypeId, @Param("minTypeId") Long minTypeId, @Param("couPrice") int couPrice, @Param("clickNum") int clickNum);
    TypeVO getType(@Param("couId") Long couId);
    String getTypeName(@Param("couTypeId") Long couTypeId);
    int getCataNumByCouId(@Param("couId") Long couId);
    void updatePurNum(@Param("couId") Long couId, @Param("purchaseNum") Long purchaseNum);
    Long getColletion(@Param("couId") Long couId);
    IPage<CouAppraiseVO> courseAppraise(@Param("couId") Long couId, IPage<CouAppraiseVO> page);
    CouAppraiseVO getCouAppraiseVO(@Param("couId") Long couId, @Param("stuId") Long stuId);
    Long getAllCouScore(@Param("couId") Long couId);
    int getTotal(@Param("couId") Long couId);
    void addCouAppraise(CouAppraiseDTO couAppraiseDTO);
    void updateCouAppraise(CouAppraiseDTO couAppraiseDTO);
    void addScore(@Param("stuId") Long stuId, @Param("scoreType") int scoreType, @Param("score") int score);
}

