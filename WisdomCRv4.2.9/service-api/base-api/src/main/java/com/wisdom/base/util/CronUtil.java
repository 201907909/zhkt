package com.wisdom.base.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author WisdomCR
 * @since 2022-05-10
 */
public class CronUtil {
    public static String getCron(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("ss mm HH dd MM ? yyyy");
        String formatTimeStr = "";
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }
}
