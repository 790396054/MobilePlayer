package com.app.gmm.mobileplyer.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gmm on 2017/3/29.
 * 时间日期工具类
 */

public class TimeUtil {
    /**
     * 得到系统时间.返回格式HH:mm:ss
     * @return
     */
    public static String getSysTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }
}
