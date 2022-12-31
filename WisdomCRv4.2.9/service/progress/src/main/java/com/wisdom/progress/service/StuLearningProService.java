package com.wisdom.progress.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wisdom.user.bean.dto.AddStuLearningProDTO;
import com.wisdom.user.bean.dto.GetStuLearningProDTO;
import com.wisdom.user.bean.vo.StuProgressVO;
import com.wisdom.user.domain.StuLearningPro;

/**
 * @author WisdomCR
 * @since 2022/11/10
 */
public interface StuLearningProService extends IService<StuLearningPro> {
    Boolean updateLearningProgress(AddStuLearningProDTO stuLearningProDTO);
    StuProgressVO getClassProgress(GetStuLearningProDTO getStuLearningProDTO);
}
