package com.wisdom.course.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wisdom.base.util.R;
import com.wisdom.base.util.RespPageBean;
import com.wisdom.chapter.bean.vo.ChapVO;
import com.wisdom.chapter.bean.vo.ChapterVO;
import com.wisdom.course.bean.dto.*;
import com.wisdom.course.bean.vo.*;
import com.wisdom.course.domain.Course;
import com.wisdom.type.bean.vo.TypeVO;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * @author WisdomCR
 * @since 2022-10-30
 */
public interface CourseService extends IService<Course> {
    R addCourse(CourseAddDTO courseAddVO);
    Boolean deleteCourseById(Long id);
    R updateCourseById(CourseUpdateDTO courseUpdateDTO);
    List<CourseAboutVO> queryCourseByClassId(Long classId);
    List<CourseVO> getCourseByCouId(List<Long> couId);
    List<Integer> getCourPriceByCouId(List<Long> couId);
    CourseVO courseDetailByCouId(Long couId);
    List<Long> getChaIdsByCouId(Long couId);
    R courseFile(MultipartFile multipartFile);
    List<CourseAboutVO> getCourseByStuId(Long stuId);
    List<CouAboutClassVO> classQueryAllCourse();
    Boolean updateCouCataNumById(Long id);
    Boolean updateCouCollNumById(CouInteractionUpDTO couInteractionUpDTO);
    Boolean updateCartState(CouInteractionUpDTO couInteractionUpDTO);
    Boolean updateCouShareNumById(CouInteractionUpDTO couInteractionUpDTO);
    Boolean updateCouClicksById(Long id);
    Boolean updateCouRecommendNumById(CouInteractionUpDTO couInteractionUpDTO);
    Boolean updateCouPurchaseById(CouInteractionUpDTO couInteractionUpDTO);
    Boolean updateEvaluateNumById(CouInteractionUpDTO couInteractionUpDTO);
    Boolean isCouPurchaseById(CouInteractionUpDTO couInteractionUpDTO);
    List<CourseBriefVO> selectCourseByClassId(Long classId);
    List<CourseBriefVO> selectAllCourse(Integer startIndex, Integer pageSize, Long classId);
    List<CourseBriefVO> selectAllCourseComplete();
    R teaQueryAllCourse(Long teaId);
    List<CourseVO> recommendCourse(Long stuId);
    List<TypeVO> recommendType();
    Integer getCouPrice(Long couId);
    String getCouIntroduction(Long couId);
    CourseDetailedVO courseDetail(Long stuId, Long couId);
    CourseDetailedVO courseDetail1(Long stuId, Long couId);
    CourseDetailedVO courseDetail2(Long stuId, Long couId);
    CourseDetailedVO courseDetail3(Long stuId, Long couId);
    List<ChapVO> allChapterByCouId(Long couId);
    RespPageBean search(String val, IPage<CouVO> page );
    List<ChapterVO> getClaCourseAllCha(Long couId);
    ClickVO click(Long stuId, Long couId);
    ClickVO cancelClick(Long stuId, Long couId);
    RespPageBean seeMore(IPage<CouVO> page, Long maxTypeId, Long minTypeId, int couPrice, int clickNum);
    void updatePurNum(Long couId, Long purchaseNum);
    Long getColletion(Long couId);
    CouAVO courseAppraise(Long couId, IPage<CouAppraiseVO> page);
    CouAppraiseVO getCouAppraiseVO(Long couId, Long stuId);
    R addCouAppraise(CouAppraiseDTO couAppraiseDTO);
    Float getCouEvSc(Long couId);
    List<String> selectCourseName(List<Long> couIds);
    R getAllAnyFieryCourse();
    List<TypeVO> getParentTypeId();
    List<TypeVO> getCouTypeByParentTypeId(Long parentTypeId);
    List<TypeVO> getFieryCouTypeId();
    List<SimpleAnyFieryCourseVO> getCourseByInfo(String info);
}
