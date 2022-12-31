package com.wisdom.user.service.impl;

import com.wisdom.course.bean.vo.CourseVO;
import com.wisdom.course.feign.RemoteCourseService;
import com.wisdom.user.mapper.ColletionMapper;
import com.wisdom.user.service.ColletionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.banner.lib.lock.RedisLock;

import java.util.ArrayList;
import java.util.List;

@Service
public class ColletionServiceImpl implements ColletionService {

    @Autowired
    private ColletionMapper colletionMapper;
    @Autowired
    private RemoteCourseService remoteCourseService;

    @Override
    public List<CourseVO> getAllColletion(Long stuId) {

        List<Long> couIds = getCouIdByStuId(stuId);

        if (couIds != null) {
            return remoteCourseService.getCourseByCouId(couIds);
        }else {
            return null;
        }

    }

    @RedisLock(name = "add", key = "#p0 + ':' + #p1")
    @Override
    public void deleted(Long stuId, Long couId) {
        Long couCollNum = remoteCourseService.getColletion(couId);
        colletionMapper.deleted(stuId, couId);
        colletionMapper.updateCouCollNum(couId, couCollNum-1);
    }

    @Override
    public CourseVO conDetailByCouId(Long couId) {
        return remoteCourseService.courseDetailByCouId(couId);
    }

    @RedisLock(name = "add", key = "#p0 + ':' + #p1")
    @Override
    public void add(Long stuId, Long couId) {
        Long couCollNum = remoteCourseService.getColletion(couId);
        colletionMapper.add(stuId, couId);
        colletionMapper.updateCouCollNum(couId, couCollNum+1);
    }

    @Override
    public Integer isCollection(Long stuId, Long couId) {
        return colletionMapper.isCollection(stuId, couId);
    }

    public ArrayList<Long> getCouIdByStuId(Long stuId) {
        return colletionMapper.getCouIdByStuId(stuId);
    }

}
