package com.wisdom.progress.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wisdom.progress.mapper.StuLearningProMapper;
import com.wisdom.progress.mapper.StudentClassMapper;
import com.wisdom.progress.service.StuLearningProService;
import com.wisdom.user.bean.dto.AddStuLearningProDTO;
import com.wisdom.user.bean.dto.GetStuLearningProDTO;
import com.wisdom.user.bean.vo.StuProgressVO;
import com.wisdom.user.domain.StuLearningPro;
import com.wisdom.user.domain.StudentClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author WisdomCR
 * @since 2022/11/10
 */
@Service
public class StuLearningProServiceImpl extends ServiceImpl<StuLearningProMapper, StuLearningPro> implements StuLearningProService {
    @Autowired
    private StuLearningProMapper stuLearningProMapper;

    @Autowired
    private StudentClassMapper studentClassMapper;

    /**
     * 更新学生在班级的学习进度
     *
     * @param stuLearningProDTO 学生学习进度DTO
     * @return {@link Boolean}
     */
    @Override
    public Boolean updateLearningProgress(AddStuLearningProDTO stuLearningProDTO) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("stu_id", stuLearningProDTO.getStuId());
        queryWrapper.eq("cou_id", stuLearningProDTO.getCouId());
        queryWrapper.eq("cha_id", stuLearningProDTO.getChaId());
        queryWrapper.select("start_time", "end_time");
        StuLearningPro stuLearningPro = stuLearningProMapper.selectOne(queryWrapper);// 判断是否有记录
        if (stuLearningPro != null) {// 有该学生的记录了，更新时长
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("stu_id", stuLearningProDTO.getStuId());
            updateWrapper.eq("cou_id", stuLearningProDTO.getCouId());
            updateWrapper.eq("cha_id", stuLearningProDTO.getChaId());

            if (stuLearningPro.getEndTime() > stuLearningProDTO.getStartTime()) {// 判断是否大于数据库记录的最长时长
                updateWrapper.set("start_time", stuLearningProDTO.getStartTime());
            } else {
                updateWrapper.set("start_time", stuLearningProDTO.getStartTime());
                updateWrapper.set("end_time", stuLearningProDTO.getStartTime());
            }
            int result = stuLearningProMapper.update(null, updateWrapper);
            if (result == 1) {
                return true;
            }
        } else {// 没有学习记录，插入
            queryWrapper.clear();
            queryWrapper.eq("stu_id", stuLearningProDTO.getStuId());
            queryWrapper.select("class_id");
            StudentClass studentClass = studentClassMapper.selectOne(queryWrapper);

            StuLearningPro learningPro = new StuLearningPro();
            learningPro.setStuId(stuLearningProDTO.getStuId())
                    .setCouId(stuLearningProDTO.getCouId())
                    .setClassId(studentClass.getClassId())
                    .setChaId(stuLearningProDTO.getChaId())
                    .setFaChaId(stuLearningProDTO.getFaChaId())
                    .setStartTime(stuLearningProDTO.getStartTime())
                    .setEndTime(stuLearningProDTO.getStartTime());
            int result = stuLearningProMapper.insert(learningPro);
            if (result == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 得到学生班级学习进度
     *
     * @param getStuLearningProDTO 获取学生在班级学习进度DTO对象
     * @return {@link StuProgressVO}
     */
    @Override
    public StuProgressVO getClassProgress(GetStuLearningProDTO getStuLearningProDTO) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("stu_id", getStuLearningProDTO.getStuId());
        queryWrapper.select("class_id");
        StudentClass studentClass = studentClassMapper.selectOne(queryWrapper);

        queryWrapper.clear();
        queryWrapper.eq("stu_id", getStuLearningProDTO.getStuId());
        queryWrapper.eq("class_id", studentClass.getClassId());
        queryWrapper.eq("cou_id", getStuLearningProDTO.getCouId());
        queryWrapper.eq("cha_id", getStuLearningProDTO.getChaId());
        queryWrapper.select("end_time");
        StuLearningPro stuLearningPro = stuLearningProMapper.selectOne(queryWrapper);
        StuProgressVO stuProgressVO = new StuProgressVO();
        stuProgressVO.setEndTime(stuLearningPro.getEndTime());
        return stuProgressVO;
    }
}
