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
 

package org.apache.mybatis.jpa.util;

import org.apache.commons.lang.SystemUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;



/**
 * Application Context
 * 
 * @author Crystal.Sea
 * @since 1.6
 */
public final class MybatisJpaContext {
	
	private static String VERSION = null;
	
	public static StandardEnvironment properties;
	
	public static ApplicationContext applicationContext = null;
	
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
		if(applicationContext == null) {
			return getApplicationContext().getBean(id);
		}else {
			return applicationContext.getBean(id);
		}
	}
	
    public static <T> T getBean(String name, Class<T> requiredType) throws BeansException{
    	if(applicationContext == null) {
            return getApplicationContext().getBean(name,requiredType);
        }else {
            return applicationContext.getBean(name,requiredType);
        }
    };
	
	//below method is common HttpServlet method
	/**
	 * get Spring HttpServletRequest
	 * @return HttpServletRequest
	 */
	public static HttpServletRequest getRequest(){
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	}

	/**
	 * get current Session
	 * @return HttpSession
	 */
	public static HttpSession getSession(){
		return getRequest().getSession();
	}

	public static String version() {
		if(VERSION == null) {
			StringBuffer version =
					new StringBuffer("---------------------------------------------------------------------------------\n");
					  version.append("+                                MaxKey \n");
					  version.append("+                      Single   Sign   On ( SSO ) \n");
					  version.append("+                           Version "); 
					  version.append(properties.getProperty("application.formatted-version")+"\n");
					  version.append("+\n");
					  version.append(String.format("+                 %sCopyright 2018 - %s https://www.maxkey.top/\n",
		        			    (char)0xA9 , new DateTime().getYear()
		        			));
					  version.append("+                 Licensed under the Apache License, Version 2.0 \n");
	
				        
					  version.append("---------------------------------------------------------------------------------\n");
					  version.append("+                                JAVA    \n");
					  version.append(String.format("+                 %s java version %s, class %s\n",
				                        SystemUtils.JAVA_VENDOR,
				                        SystemUtils.JAVA_VERSION,
				                        SystemUtils.JAVA_CLASS_VERSION
				                    ));
					  version.append(String.format("+                 %s (build %s, %s)\n",
				                        SystemUtils.JAVA_VM_NAME,
				                        SystemUtils.JAVA_VM_VERSION,
				                        SystemUtils.JAVA_VM_INFO
				                    ));
					  version.append("---------------------------------------------------------------------------------\n");
			 VERSION = version.toString();
		}
		return VERSION;
	}
}