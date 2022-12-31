package com.wisdom.stclass.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wisdom.stclass.bean.dto.ClassAddDelCouDTO;
import com.wisdom.stclass.bean.dto.ClassAddDelStuDTO;
import com.wisdom.stclass.bean.vo.DailyTimeVO;
import com.wisdom.stclass.domain.Class;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

/**
 * @author WisdomCR
 * @since 2022-10-30
 */
@Mapper
@Repository
public interface ClassMapper extends BaseMapper<Class> {
    Long classAddStu(ClassAddDelStuDTO classAddDelStuDTO);
    Long classAddCou(ClassAddDelCouDTO classAddDelCouDTO);
    Boolean classDeletedStu(ClassAddDelStuDTO classAddDelStuDTO);
    Boolean classDeletedCou(ClassAddDelCouDTO classAddDelCouDTO);
    Boolean classUpCou(ClassAddDelCouDTO classAddDelCouDTO);
    Boolean classUpStu(ClassAddDelStuDTO classAddDelStuDTO);
    Integer selectClaCou(ClassAddDelCouDTO classAddDelCouDTO);
    Integer selectClaStu(ClassAddDelStuDTO classAddDelStuDTO);
    List<DailyTimeVO> getStuDailyTime(Long stuId);
    List<Map> getStuClockDate(Long stuId);
}
