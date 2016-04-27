/*
 * 类文件名:  AccessLogFilter.java
 * 著作版权:  深圳市展云软件技术有限公司 Copyright 2012-2022, E-mail: 283912449@qq.com, All rights reserved
 * 功能描述:  <描述>
 * 类创建人:  曾云龙
 * 创建时间:  2014-12-25
 * 功能版本:  V001Z0001
 */
package com.zhanyun.framework.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhanyun.common.utils.LogUtils;

/**
 * 记录访问日志过滤器
 * 
 * @author   曾云龙
 * @version  V001Z0001
 * @date     2014-12-25
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class AccessLogFilter extends AbstractBaseFilter
{
    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws IOException, ServletException
    {
        LogUtils.logAccess(request);
        filterChain.doFilter(request, response);
    }
}
