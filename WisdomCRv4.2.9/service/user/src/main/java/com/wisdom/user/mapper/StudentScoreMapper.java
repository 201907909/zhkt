package com.wisdom.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wisdom.user.domain.StuScore;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WisdomCR
 * @since 2022/11/13
 */
@Mapper
@Repository
public interface StudentScoreMapper extends BaseMapper<StuScore> {
}
