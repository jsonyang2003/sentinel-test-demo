package com.sentinel.demo.filter;

import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.csp.sentinel.adapter.servlet.CommonFilter;

//@Configuration
public class FilterConfig {

	//@Bean
	public FilterRegistrationBean sentinelFilterRegistration() {
		FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
		registration.setFilter(new CommonFilter());
		registration.addUrlPatterns("/*");
		registration.setName("sentinelFilter");
		registration.setOrder(1);

		return registration;
	}

	/**
	 * <filter> <filter-name>SentinelCommonFilter</filter-name>
	 * <filter-class>com.alibaba.csp.sentinel.adapter.servlet.CommonFilter</filter-class>
	 * </filter>
	 * 
	 * <filter-mapping> <filter-name>SentinelCommonFilter</filter-name>
	 * <url-pattern>/*</url-pattern> </filter-mapping>
	 */
}