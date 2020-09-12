package com.pnu.dev.shpaltaif.util;

import javax.servlet.http.HttpServletRequest;

public class HttpUtils {

    public static String getPreviousPageUrl(HttpServletRequest request) {
        return request.getHeader("Referer");
    }

}
