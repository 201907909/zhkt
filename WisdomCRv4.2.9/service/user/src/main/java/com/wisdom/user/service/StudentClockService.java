package com.wisdom.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wisdom.base.util.R;
import com.wisdom.user.bean.dto.GetClockDaysOfMonthDTO;
import com.wisdom.user.bean.vo.StuClockDetailsVO;
import com.wisdom.user.domain.ClockDate;
import java.text.ParseException;

/**
 * @author WisdomCR
 * @since 2022/11/7
 */
public interface StudentClockService extends IService<ClockDate> {
    R clock(Long stu_id);// 打卡
    StuClockDetailsVO getSignInDaysOfTheMonth(GetClockDaysOfMonthDTO getClockDaysOfMonthDTO) throws ParseException;// 当月签到天数
    Boolean countersign(GetClockDaysOfMonthDTO getClockDaysOfMonthDTO);// 补签
}
