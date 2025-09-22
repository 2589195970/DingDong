package com.ruoyi.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DBUtils {

    public static final String getTableNameByDate(Date date, String pattern) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
}
