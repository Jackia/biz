/*
 * 类文件名:  LogUtils.java
 * 著作版权:  深圳市展云软件技术有限公司 Copyright 2012-2022, E-mail: 283912449@qq.com, All rights reserved
 * 功能描述:  <描述>
 * 类创建人:  曾云龙
 * 创建时间:  2014-12-23
 * 功能版本:  V001Z0001
 */
package com.zhanyun.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 日志常用类
 * 
 * @author 曾云龙
 * @version V001Z0001
 * @date 2014-12-23
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class LogUtils
{
    
    public static final Logger ERROR_LOG = LoggerFactory.getLogger("biz-error");
    
    public static final Logger ACCESS_LOG = LoggerFactory.getLogger("biz-access");
    
    /**
     * 记录访问日志 userName, jsessionid,IP,accept,UserAgent,url params, Referer
     * 
     * @author 曾云龙
     * @version V001Z0001
     * @date 2014-12-23
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static void logAccess(HttpServletRequest request)
    {
        String userName = getUserName();
        String jsessionId = request.getRequestedSessionId();
        String ip = IpUtils.getIpAddress(request);
        String accept = request.getHeader("accept");
        String userAgent = request.getHeader("User-Agent");
        String url = request.getRequestURI();
        String params = getParams(request);
        String headers = getHeaders(request);
        
        StringBuilder sb = new StringBuilder();
        sb.append(getBlock(userName));
        sb.append(getBlock(jsessionId));
        sb.append(getBlock(ip));
        sb.append(getBlock(accept));
        sb.append(getBlock(userAgent));
        sb.append(getBlock(url));
        
        sb.append(getBlock(params));
        sb.append(getBlock(headers));
        sb.append(getBlock(request.getHeader("Referer")));
        getAccessLog().info(sb.toString());
        
    }
    
    /**
     * 
     * 记录异常错误
     * 
     * @author 曾云龙
     * @version V001Z0001
     * @date 2014-12-23
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static void logError(String message, Throwable e)
    {
        String userName = getUserName();
        StringBuilder sb = new StringBuilder();
        sb.append(getBlock("exception"));
        sb.append(getBlock(userName));
        sb.append(getBlock(message));
        getErrorLog().error(sb.toString(), e);
    }
    
    /**
     * 
     * 记录页面错误 错误日志记录
     * 
     * @author 曾云龙
     * @version V001Z0001
     * @date 2014-12-23
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static void logPageError(HttpServletRequest request)
    {
        String userName = getUserName();
        
        Integer statusCode = (Integer)request.getAttribute("javax.servlet.error.status_code");
        String message = (String)request.getAttribute("javax.servlet.error.message");
        String uri = (String)request.getAttribute("javax.servlet.error.request_uri");
        Throwable throwable = (Throwable)request.getAttribute("javax.servlet.error.exception");
        if (statusCode == null)
        {
            statusCode = 0;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(getBlock(throwable == null ? "page" : "exception"));
        sb.append(getBlock(userName));
        sb.append(getBlock(statusCode));
        sb.append(getBlock(message));
        sb.append(getBlock(IpUtils.getIpAddress(request)));
        sb.append(getBlock(uri));
        sb.append(getBlock(request.getHeader("Referer")));
        
        StringWriter sw = new StringWriter();
        while (throwable != null)
        {
            throwable.printStackTrace(new PrintWriter(sw));
            throwable = throwable.getCause();
        }
        
        sb.append(getBlock(sw.toString()));
        getErrorLog().error(sb.toString());
    }
    
    /**
     * 
     * 空白日志处理
     * 
     * @author 曾云龙
     * @version V001Z0001
     * @date 2014-12-23
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static String getBlock(Object msg)
    {
        if (msg == null)
        {
            return "";
        }
        
        return "[ " + msg.toString() + " ]";
    }
    
    /**
     * 
     * 将request Map对象利用阿里Json转换器转换成Json字符串
     * 
     * @author 曾云龙
     * @version V001Z0001
     * @date 2014-12-23
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    protected static String getParams(HttpServletRequest request)
    {
        Map<String, String[]> params = request.getParameterMap();
        return JSON.toJSONString(params);
    }
    
    /**
     * 
     * 获取客户端request请求头部描述信息
     * 
     * @author 曾云龙
     * @version V001Z0001
     * @date 2014-12-23
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    private static String getHeaders(HttpServletRequest request)
    {
        Map<String, List<String>> headers = Maps.newHashMap();
        Enumeration<String> namesEnumeration = request.getHeaderNames();
        while (namesEnumeration.hasMoreElements())
        {
            String name = namesEnumeration.nextElement();
            Enumeration<String> valuesEnumeration = request.getHeaders(name);
            List<String> values = Lists.newArrayList();
            while (valuesEnumeration.hasMoreElements())
            {
                values.add(valuesEnumeration.nextElement());
            }
            headers.put(name, values);
        }
        
        return JSON.toJSONString(headers);
    }
    
    /**
     * 获取登陆账户信息
     * 
     * @author 曾云龙
     * @version V001Z0001
     * @date 2014-12-23
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    protected static String getUserName()
    {
        return (String)SecurityUtils.getSubject().getPrincipal();
    }
    
    public static Logger getErrorLog()
    {
        return ERROR_LOG;
    }
    
    public static Logger getAccessLog()
    {
        return ACCESS_LOG;
    }
    
}
