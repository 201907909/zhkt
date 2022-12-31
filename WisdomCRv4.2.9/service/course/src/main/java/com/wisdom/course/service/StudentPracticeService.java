package com.wisdom.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wisdom.base.util.R;
import com.wisdom.practice.bean.dto.StuPracticeDTO;
import com.wisdom.practice.domain.Practice;

public interface StudentPracticeService extends IService<Practice> {
    R studentIsJoinPractice(StuPracticeDTO stuPracticeDTO);
    R studentSubmitPractice(StuPracticeDTO stuPracticeDTO);
    R getAllPracticeSimpleInfo(Long couId);
    R getPracticeSimpleInfo(Long id, Long stuId);
    R getPracticeSimpleByInfo(String info, Long couId);
}
