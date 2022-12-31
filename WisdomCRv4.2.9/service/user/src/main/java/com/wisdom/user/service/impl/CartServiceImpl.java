package com.wisdom.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wisdom.base.util.R;
import com.wisdom.course.bean.vo.CourseVO;
import com.wisdom.course.feign.RemoteCourseService;
import com.wisdom.user.domain.Student;
import com.wisdom.user.mapper.CartMapper;
import com.wisdom.user.mapper.StudentMapper;
import com.wisdom.user.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.banner.lib.lock.RedisLock;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private RemoteCourseService remoteCourseService;
    @Value("${deductType}")
    private int deductType;

    public List<Long> getCouIdByStuId(Long stuId) {
        return cartMapper.getCouIdByStuId(stuId);
    }

    @Override
    public List<CourseVO> getCourseByCouId(Long stuId) {

        List<Long> couIds = getCouIdByStuId(stuId);

        System.out.println(couIds);
        if (couIds.size() != 0) {
            return remoteCourseService.getCourseByCouId(couIds);
        }else {
            return null;
        }
    }

    @Override
    public void deletedCartByCouId(Long stuId, Long couId) {
        cartMapper.deletedCartByCouId(stuId, couId);
    }

    /**
     *添加到stu_cou,同时删除stu_cou_cart
     */
    @RedisLock(name = "buyCartByCouId", key = "#p0 + ':' + #p1")
    @Override
    public R buyCartByCouId(Long stuId, List<Long> couId) {

        Integer stuScore = studentMapper.selectOne(new LambdaQueryWrapper<>(Student.class).eq(Student::getStuId, stuId)).getStuScore();

        List<Integer> couPrices = remoteCourseService.getCourPriceByCouId(couId);

        int couPriceTotal = 0;

        for (int i=0;i<couPrices.size();i++) {
            couPriceTotal += couPrices.get(i);
        }

        if (stuScore < couPriceTotal) {
            return R.error("积分不够");
        }else {
            studentMapper.updateScore(stuId, stuScore-couPriceTotal);
        }

        for (int i=0;i<couId.size();i++) {

            CourseVO courseVO = remoteCourseService.courseDetailByCouId(couId.get(i));

            Long purchaseNum = courseVO.getPurchaseNum();

            cartMapper.addDeScore(stuId, 6, couPrices.get(i), deductType);

            remoteCourseService.updatePurNum(courseVO.getCouId(), purchaseNum+1);

            cartMapper.buy(stuId, couId.get(i));

            deletedCartByCouId(stuId, couId.get(i));
        }
        return R.ok("购买成功");
    }

    /**
     *传couId查看详情课程
     */
    @Override
    public CourseVO cartDetailByCouId(Long couId) {
        return remoteCourseService.courseDetailByCouId(couId);
    }

    @Override
    public void add(Long stuId, Long couId) {
        cartMapper.add(stuId, couId);
    }

    @Override
    public Integer isAddCart(Long stuId, Long couId) {
        return cartMapper.isAddCart(stuId, couId);
    }

    @Override
    public Integer isBuy(Long stuId, Long couId) {
        return cartMapper.isBuy(stuId, couId);
    }

    @Override
    public void refund(Long stuId, Long couId) {
        cartMapper.refund(stuId, couId);
    }
}
