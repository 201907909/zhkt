package com.wisdom.progress.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wisdom.user.bean.dto.AddStuCourseProDTO;
import com.wisdom.user.bean.dto.GetStuCourseProDTO;
import com.wisdom.user.bean.dto.GetStuLearningProDTO;
import com.wisdom.user.bean.vo.StuProgressVO;
import com.wisdom.user.domain.StuCoursePro;

/**
 * @author WisdomCR
 * @since 2022/11/10
 */
public interface StuCourseProService extends IService<StuCoursePro> {
    Boolean updateCourseProgress(AddStuCourseProDTO stuCourseProDTO);
    StuProgressVO getCourseProgress(GetStuCourseProDTO getStuCourseProDTO);
}
