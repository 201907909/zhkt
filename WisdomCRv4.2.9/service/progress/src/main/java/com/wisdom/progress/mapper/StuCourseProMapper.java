package com.wisdom.progress.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wisdom.user.domain.StuCoursePro;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WisdomCR
 * @since 2022/11/10
 */
@Mapper
@Repository
public interface StuCourseProMapper extends BaseMapper<StuCoursePro> {
}
