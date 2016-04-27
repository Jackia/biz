/*
 * 类文件名:  IpUtils.java
 * 著作版权:  深圳市展云软件技术有限公司 Copyright 2012-2022, E-mail: 283912449@qq.com, All rights reserved
 * 功能描述:  <描述>
 * 类创建人:  曾云龙
 * 创建时间:  2014-12-24
 * 功能版本:  V001Z0001
 */
package com.zhanyun.common.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取IP常用类
 * 
 * @author 曾云龙
 * @version V001Z0001
 * @date 2014-12-24
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public final class IpUtils
{
    public static String getIpAddress(HttpServletRequest request)
    {
        if(request == null)
        {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("X-Forwarded-For");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
