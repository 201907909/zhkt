package com.wisdom.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wisdom.user.domain.ClockDate;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WisdomCR
 * @since 2022/11/7
 */
@Mapper
@Repository
public interface StudentClockMapper extends BaseMapper<ClockDate> {
}
