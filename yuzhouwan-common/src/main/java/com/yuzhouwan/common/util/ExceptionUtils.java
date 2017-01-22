package com.yuzhouwan.common.util;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：Exception Utils
 *
 * @author Benedict Jin
 * @since 2016/11/24
 */
public class ExceptionUtils {

    public static String errorInfo(Exception e) {
        return errorInfo(e, null);
    }

    public static String errorInfo(Exception e, String detail) {
        if (e == null) return null;
        Class<?> clazz;
        String msg;
        return String.format("%s: %s%s", (clazz = e.getClass()) == null ? "" : clazz.getSimpleName(),
                StrUtils.isEmpty(msg = e.getMessage()) ? "[No More Detail Info]" : msg,
                StrUtils.isEmpty(detail) ? "" : ", Detail: ".concat(detail));
    }
}