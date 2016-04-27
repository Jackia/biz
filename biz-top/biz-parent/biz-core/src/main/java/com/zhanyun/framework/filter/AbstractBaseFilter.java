/*
 * 类文件名:  AbstractBaseFilter.java
 * 著作版权:  深圳市展云软件技术有限公司 Copyright 2012-2022, E-mail: 283912449@qq.com, All rights reserved
 * 功能描述:  <描述>
 * 类创建人:  曾云龙
 * 创建时间:  2014-12-25
 * 功能版本:  V001Z0001
 */
package com.zhanyun.framework.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * 配置黑白名单过滤器
 * 
 * @author 曾云龙
 * @version V001Z0001
 * @date 2014-12-25
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public abstract class AbstractBaseFilter implements Filter
{
    private FilterConfig config = null;
    
    private final String[] NULL_STRING_ARRAY = new String[0];
    
    /**
     * 逗号，空格，分号，换行
     */
    private final String URL_SPLIT_PATTERN = "[, ;\r\n]";
    
    private final PathMatcher pathMatcher = new AntPathMatcher();
    
    private final Logger logger = LoggerFactory.getLogger("url.filter");
    
    /**
     * 白名单
     */
    private String[] whiteListURLs = null;
    
    /**
     * 黑名单
     */
    private String[] blackListURLs = null;
    
    @Override
    public void init(FilterConfig config)
        throws ServletException
    {
        this.config = config;
        this.initConfig();
        this.ini();
        
    }
    
    @Override
    public final void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
        throws IOException, ServletException
    {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;
        
        String currentURL = httpServletRequest.getServletPath();
        logger.debug("url filter: current url is : [{}]", currentURL);
        
        if(isBlackURL(currentURL))
        {
            filterChain.doFilter(request, response);
            return;
        }
        
        if(!isWhiteURL(currentURL))
        {
            filterChain.doFilter(request, response);
            return;
        }
        
        doFilter(httpServletRequest, httpServletResponse, filterChain);
        return;
    }
    
    private boolean isWhiteURL(String currentURL)
    {
        for(String whiteURL : whiteListURLs)
        {
            if(pathMatcher.match(whiteURL, currentURL))
            {
               logger.debug("url filter : white url list maches : [{}] match [{}] continue.",currentURL, whiteURL);
               return true; 
            }
        }
        logger.debug("url filter : white url list not maches : [{}] not match [{}] continue.",currentURL, Arrays.toString(whiteListURLs));
        return false;
    }

    private boolean isBlackURL(String currentURL)
    {
        for(String blackURL : blackListURLs)
        {
            if(pathMatcher.match(blackURL, currentURL))
            {
               logger.debug("url filter : black url list maches : [{}] match [{}] continue.",currentURL, blackURL);
               return true; 
            }
        }
        
        logger.debug("url filter : black url list not maches : [{}] not match [{}] continue.",currentURL, Arrays.toString(blackListURLs));
        
        return false;
    }

    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException
    {
        filterChain.doFilter(request, response);
    }
    
    @Override
    public void destroy()
    {
        
    }
    
    private void initConfig()
    {
        String whiteListURLStr = this.config.getInitParameter("whiteListURL");
        whiteListURLs = strToArray(whiteListURLStr);
        
        String blackListURLStr = this.config.getInitParameter("blackListURL");
        blackListURLs = strToArray(blackListURLStr);
    }
    
    private String[] strToArray(String urlStr)
    {
        if(urlStr == null)
        {
            return NULL_STRING_ARRAY;
        }
        String[] urlArray = urlStr.split(URL_SPLIT_PATTERN);
        List<String> urlList = new ArrayList<String>();
        for(String url : urlArray)
        {
            url = url.trim();
            if(url.length() == 0)
            {
                continue;
            }
            
            urlList.add(url);
        }
        return urlList.toArray(NULL_STRING_ARRAY);
    }

    public void ini() throws ServletException
    {
        
    }
}
