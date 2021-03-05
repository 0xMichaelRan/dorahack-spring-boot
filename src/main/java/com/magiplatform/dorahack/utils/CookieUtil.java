package com.magiplatform.dorahack.utils;


import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @version V1.0
 * @Title: CookieUtil
 * @Package com.magiplatform.dorahack.utils
 * @Description: CookieUtil
 * @date 2020/8/26 10:53
 */
public class CookieUtil {
    public CookieUtil() {
    }

    public static void addCookie(String name, String value, String domain, int maxage, String path, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        if (domain != null) {
            cookie.setDomain(domain);
        }

        cookie.setMaxAge(maxage);
        cookie.setPath(path);
        response.addCookie(cookie);
    }

    public static void addCookie(String name, String value, String domain, int maxage, HttpServletResponse response) {
        addCookie(name, value, domain, maxage, "/", response);
    }

    public static String getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        String headerValue = request.getHeader(name);
        if (StringUtils.isNotBlank(headerValue)) {
            return headerValue;
        }
        if (cookies != null) {
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(name)){
                    return cookie.getValue();
                }
            }
        }
        return "";
    }


    public static void removeCookie(String name, String domain, HttpServletRequest request, HttpServletResponse response) {
        String cookieVal = getCookie(request, name);
        if (cookieVal != null) {
            addCookie(name, (String) null, domain, 0, response);
        }

    }



    public static void removeCookie(String name, HttpServletRequest request, HttpServletResponse response) {
        removeCookie(name, ".socialshops.com", request, response);
    }
}