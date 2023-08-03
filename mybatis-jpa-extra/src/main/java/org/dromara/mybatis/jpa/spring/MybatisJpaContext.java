/*
 * Copyright [2022] [MaxKey of copyright http://www.maxkey.top]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 

package org.dromara.mybatis.jpa.spring;

import java.time.LocalDateTime;

import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Mybatis Jpa Context
 * 
 * @author Crystal.Sea
 * @since 3.0
 */
public final class MybatisJpaContext {

	private static final Logger logger = LoggerFactory.getLogger(MybatisJpaContext.class);
	
	private static String VERSION = null;
	
	private static StandardEnvironment properties;
	
	private static ApplicationContext mybatisJpaContext = null;
	
	/**
	 * init mybatisJpaContext and properties
	 * 
	 * @param applicationContext
	 */
	public static void init(ApplicationContext applicationContext) {
		
		mybatisJpaContext = applicationContext;
		
		if (mybatisJpaContext.containsBean("propertySourcesPlaceholderConfigurer")) {
			logger.trace("init MybatisJpaContext properties");
            PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = 
                    ((PropertySourcesPlaceholderConfigurer) applicationContext
                    .getBean("propertySourcesPlaceholderConfigurer"));
            
            properties =  (StandardEnvironment) propertySourcesPlaceholderConfigurer
                    .getAppliedPropertySources()
                    .get(PropertySourcesPlaceholderConfigurer.ENVIRONMENT_PROPERTIES_PROPERTY_SOURCE_NAME)
                    .getSource();
		}
		
	}
	
	/**
	 * get ApplicationContext from web  ServletContext configuration
	 * @return ApplicationContext
	 */
	public static ApplicationContext getApplicationContext(){
		return WebApplicationContextUtils.getWebApplicationContext(getSession().getServletContext());
	}
	
	/**
	 * get bean from spring configuration by bean id
	 * @param id
	 * @return Object
	 */
	public static Object getBean(String id){
		if(mybatisJpaContext == null) {
			return getApplicationContext().getBean(id);
		}else {
			return mybatisJpaContext.getBean(id);
		}
	}
	
    public static <T> T getBean(String name, Class<T> requiredType) throws BeansException{
    	if(mybatisJpaContext == null) {
            return getApplicationContext().getBean(name,requiredType);
        }else {
            return mybatisJpaContext.getBean(name,requiredType);
        }
    };
	
	//below method is common HttpServlet method
	/**
	 * get Spring HttpServletRequest
	 * @return HttpServletRequest
	 */
	public static HttpServletRequest getRequest(){
		ServletRequestAttributes servletRequestAttributes =(ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		if(servletRequestAttributes !=  null) {
			return servletRequestAttributes.getRequest();
		}else {
			return null;
		}
	}

	/**
	 * get current Session
	 * @return HttpSession
	 */
	public static HttpSession getSession(){
		return getRequest().getSession();
	}

	public static StandardEnvironment getProperties() {
		return properties;
	}

	public static ApplicationContext getMybatisJpaContext() {
		return mybatisJpaContext;
	}

	public static String version() {
		if(VERSION == null) {
			return 
					String.format("""
						---------------------------------------------------------------------------------
						-              JAVA    
						-              %s java version %s, class %s
						-              %s (build %s, %s)
						---------------------------------------------------------------------------------
						-                                MyBatis JPA Extra 
						-						                                
						-              %sCopyright 2018 - %s https://gitee.com/dromara/mybatis-jpa-extra/
						-
						-              Licensed under the Apache License, Version 2.0 
						---------------------------------------------------------------------------------
						""",
						SystemUtils.JAVA_VENDOR,
						SystemUtils.JAVA_VERSION,
						SystemUtils.JAVA_CLASS_VERSION,
						SystemUtils.JAVA_VM_NAME,
						SystemUtils.JAVA_VM_VERSION,
						SystemUtils.JAVA_VM_INFO,
						(char)0xA9,
						LocalDateTime.now().getYear()
					);
		}
		return VERSION;
	}
	
	
}
