package com.example.copytext;

import android.content.Context;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 防抖工具
 * Created by Lost on 2018/3/8.
 */

public class AntiShakeUtil {
    private static List<OneClickUtil> utils = new ArrayList<>();
    private static String pageTag = "";

    public static boolean check(Object o, Context context) {
        if (pageTag == null) {
            pageTag = context.getClass().getName();
        } else if (!pageTag.equals(context.getClass().getName())) {
            utils.clear();
            pageTag = context.getClass().getName();
        }
        String flag;
        if (o == null)
            flag = Thread.currentThread().getStackTrace()[2].getMethodName();
        else
            flag = o.toString();
        for (OneClickUtil util : utils) {
            if (util.getMethodName().equals(flag)) {
                return util.check();
            }
        }

        if (flag.contains(pageTag))
            return true;

        OneClickUtil clickUtil = new OneClickUtil(flag);
        utils.add(clickUtil);
        return clickUtil.check();
    }


    /**
     * 一次点击间隔时间
     */
    static class OneClickUtil {
        private String methodName;
        public static final int MIN_CLICK_DELAY_TIME = 1000;
        private long lastClickTime = 0;

        public OneClickUtil(String methodName) {
            this.methodName = methodName;
        }

        public String getMethodName() {
            return methodName;
        }

        public boolean check() {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                return false;
            } else {
                return true;
            }
        }
    }
}
