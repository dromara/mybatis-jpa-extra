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

import org.dromara.mybatis.jpa.metadata.MapperMetadata;
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
    static final Logger logger = LoggerFactory.getLogger(MybatisJpaContext.class);
    
    static StandardEnvironment properties;
    
    static ApplicationContext jpaContext;
    
    static String propertyConfigurerBeanName = "propertySourcesPlaceholderConfigurer";
    
    private MybatisJpaContext() {}
    
    /**
     * init mybatisJpaContext and properties
     * 
     * @param applicationContext
     */
    public static void init(ApplicationContext applicationContext) {
        
        jpaContext = applicationContext;
        
        if (jpaContext.containsBean(propertyConfigurerBeanName)) {
            logger.trace("init MybatisJpaContext properties");
            PropertySourcesPlaceholderConfigurer propertyConfigurer = 
                    ((PropertySourcesPlaceholderConfigurer) applicationContext.getBean(propertyConfigurerBeanName));
            
            properties = (StandardEnvironment) propertyConfigurer
                    .getAppliedPropertySources()
                    .get(PropertySourcesPlaceholderConfigurer.ENVIRONMENT_PROPERTIES_PROPERTY_SOURCE_NAME)
                    .getSource();
        }
        
    }
    
    public static void setMapUnderscoreToCamelCase(boolean mapUnderscoreToCamelCase) {
        MapperMetadata.setMapUnderscoreToCamelCase(mapUnderscoreToCamelCase);
    }
    
    /**
     * get ApplicationContext from web  ServletContext configuration
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext(){
        HttpSession session = getSession();
        return ((session == null) ? null : WebApplicationContextUtils.getWebApplicationContext(session.getServletContext()));
    }
    
    /**
     * get bean from spring configuration by bean id
     * @param id
     * @return Object
     */
    public static Object getBean(String id){
        if(jpaContext == null) {
            ApplicationContext applicationContext = getApplicationContext();
            return ((applicationContext == null) ? null:applicationContext.getBean(id));
        }else {
            return jpaContext.getBean(id);
        }
    }
    
    public static <T> T getBean(Class<T> requiredType){
        if(jpaContext == null) {
            ApplicationContext applicationContext = getApplicationContext();
            return ((applicationContext == null) ? null: applicationContext.getBean(requiredType));
        }else {
            return jpaContext.getBean(requiredType);
        }
    }
    
    public static <T> T getBean(String name, Class<T> requiredType) throws BeansException{
        if(jpaContext == null) {
            ApplicationContext applicationContext = getApplicationContext();
            return ((applicationContext == null) ? null : applicationContext.getBean(name,requiredType));
        }else {
            return jpaContext.getBean(name,requiredType);
        }
    }
    
    //below method is common HttpServlet method
    /**
     * get Spring HttpServletRequest
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest(){
        ServletRequestAttributes servletRequestAttributes =(ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        return ((servletRequestAttributes == null )? null : servletRequestAttributes.getRequest());
    }

    /**
     * get current Session
     * @return HttpSession
     */
    public static HttpSession getSession(){
        HttpServletRequest request  = getRequest();
        return ((request== null) ? null : request.getSession());
    }

    public static StandardEnvironment getProperties() {
        return properties;
    }

    public static ApplicationContext getJpaContext() {
        return jpaContext;
    }

}
