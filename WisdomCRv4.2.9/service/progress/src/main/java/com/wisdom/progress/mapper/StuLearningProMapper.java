package com.wisdom.progress.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wisdom.user.domain.StuLearningPro;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface StuLearningProMapper extends BaseMapper<StuLearningPro> {
}
