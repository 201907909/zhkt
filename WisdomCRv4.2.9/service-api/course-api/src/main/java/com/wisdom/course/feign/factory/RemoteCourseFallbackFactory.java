package com.wisdom.course.feign.factory;

import com.wisdom.course.bean.vo.CourseAboutVO;
import com.wisdom.course.bean.vo.CourseBriefVO;
import com.wisdom.course.bean.vo.CourseVO;
import com.wisdom.course.feign.RemoteCourseService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@Slf4j
@Component
public class RemoteCourseFallbackFactory implements FallbackFactory<RemoteCourseService> {
    @Override
    public RemoteCourseService create(Throwable throwable) {
        return new RemoteCourseService() {
            @Override
            public List<String> selectCourseName(List<Long> couIds) {
                return null;
            }
            @Override
            public List<CourseBriefVO> selectAllCourseComplete() {
                return null;
            }
            @Override
            public List<CourseBriefVO> selectAllCourse(Integer startIndex, Integer pageSize, Long classId) {
                return null;
            }
            @Override
            public List<CourseBriefVO> selectCourseByClassId(Long classId) {
                return null;
            }
            @Override
            public List<CourseAboutVO> queryCourseByClassId(Long classId) {
                return null;
            }
            @Override
            public List<CourseVO> getCourseByCouId(List<Long> couId) {
                return null;
            }

            @Override
            public List<Integer> getCourPriceByCouId(List<Long> couId) {
                return null;
            }

            @Override
            public CourseVO courseDetailByCouId(Long couId) {
                return null;
            }
            @Override
            public List<Long> getChaIdsByCouId(Long couId) {
                return null;
            }
            @Override
            public Long getStuCouProgress(Long couId, Long stuId) {
                return null;
            }
            @Override
            public Integer getCouPrice(Long couId) {
                return null;
            }
            @Override
            public String getCouIntroduction(Long couId) {
                return null;
            }
            @Override
            public void updatePurNum(Long couId, Long purchaseNum) {}
            @Override
            public Long getColletion(Long couId) {
                return null;
            }
            @Override
            public Float getCouEvSc(Long couId) {
                return null;
            }
        };
    }
}
