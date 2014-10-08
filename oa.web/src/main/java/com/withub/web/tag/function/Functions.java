package com.withub.web.tag.function;

import com.withub.common.util.DateUtil;

import java.util.Date;

public class Functions {

    public static long diffDays(Date date) {
        return DateUtil.getDiffDays(date, new Date());
    }

    public static long diffHours(Date date) {
        return DateUtil.getDiffHours(date, new Date());
    }

    public static long diffMinutes(Date date) {
        return DateUtil.getDiffMinutes(date, new Date());
    }
}
