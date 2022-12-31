package com.wisdom.course.feign;

import com.wisdom.course.bean.vo.CourseAboutVO;
import com.wisdom.course.bean.vo.CourseBriefVO;
import com.wisdom.course.bean.vo.CourseVO;
import com.wisdom.course.feign.factory.RemoteCourseFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@FeignClient(name = "course",value = "course", fallbackFactory = RemoteCourseFallbackFactory.class)
public interface RemoteCourseService {
    @GetMapping("/course/selectCourseName")
    List<String> selectCourseName(@RequestBody List<Long> couIds);
    @GetMapping("/course/selectAllCourseComplete")
    List<CourseBriefVO> selectAllCourseComplete();
    @PostMapping("/course/selectAllCourse")
    List<CourseBriefVO> selectAllCourse(@RequestParam("startIndex") Integer startIndex, @RequestParam("pageSize") Integer pageSize, @RequestParam("classId") Long classId);
    @GetMapping("/course/selectCourseByClassId")
    List<CourseBriefVO> selectCourseByClassId(@RequestParam("classId") Long classId);
    @GetMapping("/course/queryCourseByClassId")
    List<CourseAboutVO> queryCourseByClassId(@RequestParam("classId") Long classId);
    @GetMapping("/course/getCourseByCouId")
    List<CourseVO> getCourseByCouId(@RequestBody List<Long> couId);
    @PostMapping("/course/getCourPriceByCouId")
    List<Integer> getCourPriceByCouId(@RequestBody List<Long> couId);
    @GetMapping("/course/courseDetailByCouId")
    CourseVO courseDetailByCouId(@RequestParam("couId") Long couId);
    @GetMapping("/course/getChaIdsByCouId")
    List<Long> getChaIdsByCouId(@RequestParam("couId") Long couId);
    @GetMapping("/course/getStuCouProgress")
    Long getStuCouProgress(@RequestParam("couId") Long couId, @RequestParam("stuId") Long stuId);
    @GetMapping("/course/getCouPrice")
    Integer getCouPrice(@RequestParam("couId") Long couId);
    @GetMapping("/course/getCouIntroduction")
    String getCouIntroduction(@RequestParam("couId") Long couId);
    @GetMapping("/course/updatePurNum")
    void updatePurNum(@RequestParam("couId") Long couId, @RequestParam("purchaseNum") Long purchaseNum);
    @GetMapping("/course/getColletion")
    Long getColletion(@RequestParam("couId") Long couId);
    @GetMapping("/course/getCouEvSc")
    Float getCouEvSc(@RequestParam("couId") Long couId);
}
