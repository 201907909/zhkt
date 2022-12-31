package com.wisdom.user.service;

import com.wisdom.base.util.R;
import com.wisdom.course.bean.vo.CourseVO;

import java.util.List;

public interface CartService {
    List<CourseVO> getCourseByCouId(Long stuId);
    void deletedCartByCouId(Long stuId, Long couId);
    R buyCartByCouId(Long stuId, List<Long> couId);
    CourseVO cartDetailByCouId(Long couId);
    void add(Long stuId, Long couId);
    Integer isAddCart(Long stuId, Long couId);

    Integer isBuy(Long stuId, Long couId);
    void refund(Long stuId, Long couId);
}
