package com.wisdom.progress.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wisdom.progress.mapper.StuCourseProMapper;
import com.wisdom.progress.service.StuCourseProService;
import com.wisdom.user.bean.dto.AddStuCourseProDTO;
import com.wisdom.user.bean.dto.GetStuCourseProDTO;
import com.wisdom.user.bean.dto.GetStuLearningProDTO;
import com.wisdom.user.bean.vo.StuProgressVO;
import com.wisdom.user.domain.StuCoursePro;
import com.wisdom.user.domain.StuLearningPro;
import com.wisdom.user.domain.StudentClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author WisdomCR
 * @since 2022/11/10
 */
@Service
public class StuCourseProServiceImpl extends ServiceImpl<StuCourseProMapper, StuCoursePro> implements StuCourseProService {
    @Autowired
    private StuCourseProMapper stuCourseProMapper;

    /**
     * 更新学生购买课程的学习进度
     *
     * @param stuCourseProDTO 学生购买课程学习进度DTO
     * @return {@link Boolean}
     */
    @Override
    public Boolean updateCourseProgress(AddStuCourseProDTO stuCourseProDTO) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("stu_id", stuCourseProDTO.getStuId());
        queryWrapper.eq("cou_id", stuCourseProDTO.getCouId());
        queryWrapper.eq("cha_id", stuCourseProDTO.getChaId());
        queryWrapper.select("start_time", "end_time");
        StuCoursePro stuCoursePro = stuCourseProMapper.selectOne(queryWrapper);// 判断是否有记录
        if (stuCoursePro != null) {// 有该学生的记录了，更新时长
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("stu_id", stuCourseProDTO.getStuId());
            updateWrapper.eq("cou_id", stuCourseProDTO.getCouId());
            updateWrapper.eq("cha_id", stuCourseProDTO.getChaId());

            if (stuCoursePro.getEndTime() > stuCourseProDTO.getStartTime()) {// 判断是否大于数据库记录的最长时长
                updateWrapper.set("start_time", stuCourseProDTO.getStartTime());
            } else {
                updateWrapper.set("start_time", stuCourseProDTO.getStartTime());
                updateWrapper.set("end_time", stuCourseProDTO.getStartTime());
            }
            int result = stuCourseProMapper.update(null, updateWrapper);
            if (result == 1) {
                return true;
            }
        } else {// 没有学习记录，插入
            StuCoursePro stuCoursePro_ = new StuCoursePro();
            stuCoursePro_.setStuId(stuCourseProDTO.getStuId())
                    .setCouId(stuCourseProDTO.getCouId())
                    .setChaId(stuCourseProDTO.getChaId())
                    .setFaChaId(stuCourseProDTO.getFaChaId())
                    .setStartTime(stuCourseProDTO.getStartTime())
                    .setEndTime(stuCourseProDTO.getStartTime());
            int result = stuCourseProMapper.insert(stuCoursePro_);
            if (result == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 得到学生购买课程学习进度
     *
     * @param getStuCourseProDTO 获取学生购买课程学习进度DTO对象
     * @return {@link StuProgressVO}
     */
    @Override
    public StuProgressVO getCourseProgress(GetStuCourseProDTO getStuCourseProDTO) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("stu_id", getStuCourseProDTO.getStuId());
        queryWrapper.eq("cou_id", getStuCourseProDTO.getCouId());
        queryWrapper.eq("cha_id", getStuCourseProDTO.getChaId());
        queryWrapper.select("end_time");
        StuCoursePro stuCoursePro = stuCourseProMapper.selectOne(queryWrapper);
        StuProgressVO stuProgressVO = new StuProgressVO();
        stuProgressVO.setEndTime(stuCoursePro.getEndTime());
        return stuProgressVO;
    }
}
