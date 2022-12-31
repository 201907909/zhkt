package com.wisdom.user.util;

import java.util.Calendar;

/**
 * @author WisdomCR
 * @since 2022/11/7
 */
public class timeDifference {
    /**
     * 用于设置redis过期时间，让用户打卡
     * 计算第二天凌晨与当前时间的时间差秒数
     *
     * @return {@link Long}
     */
    public Long getNowToNextDaySeconds() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }
}
