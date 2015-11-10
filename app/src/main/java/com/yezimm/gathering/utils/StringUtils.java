package com.yezimm.gathering.utils;

/**
 * Created by scofield on 2015/5/30.
 */
public class StringUtils {

    public static boolean isVaild(String str) {
        if (str == null || str.equals(""))
            return false;
        else
            return true;
    }
}
