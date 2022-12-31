package com.wisdom.interaction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wisdom.interlocution.domain.Interlocution;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WisdomCR
 * @since 2022-10-30
 */
@Mapper
@Repository
public interface InterlocutionMapper extends BaseMapper<Interlocution> {

}
