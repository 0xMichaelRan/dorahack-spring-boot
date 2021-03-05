package com.magiplatform.dorahack.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CrossDomainFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse)servletResponse;
//        HttpServletRequest req = (HttpServletRequest)servletRequest;
//        // 获取真实ip
//        String ip = req.getHeader("x-forwarded-for");
//        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = req.getHeader("Proxy-Client-IP");
//        }
//        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = req.getHeader("WL-Proxy-Client-IP");
//        }
//        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = req.getRemoteAddr();
//        }
//        //获取请求的源
//        String url = req.getHeader("Origin");
        // logger.info("请求的源为："+ url);
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Headers", "X-Requested-With, accept, content-type, xxxx");
        resp.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        filterChain.doFilter(servletRequest, resp);
    }

    @Override
    public void destroy() {

    }
}
