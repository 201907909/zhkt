package com.wisdom.user.service;

import com.wisdom.course.bean.vo.CourseVO;

import java.util.List;

public interface ColletionService {
    List<CourseVO> getAllColletion(Long stuId);
    void deleted(Long stuId, Long couId);
    CourseVO conDetailByCouId(Long couId);
    void add(Long stuId, Long couId);
    Integer isCollection(Long stuId, Long couId);
}
