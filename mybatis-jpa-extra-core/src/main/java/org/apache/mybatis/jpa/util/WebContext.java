package org.apache.mybatis.jpa.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;



/**
 * Application Context
 * 
 * @author Crystal.Sea
 * @since 1.6
 */
public final class WebContext {
	
	public static ApplicationContext applicationContext=null;
	
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
		if(applicationContext==null) {
			return getApplicationContext().getBean(id);
		}else {
			return applicationContext.getBean(id);
		}
	}
	
	
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
	
	/**
	 * get current Session,if no session ,new Session created
	 * @return HttpSession
	 */
	public static HttpSession getSession(boolean create) {
		return getRequest().getSession(create);
	}
	
	/**
	 * set Attribute to session ,Attribute name is name,value is value
	 * @param name
	 * @param value
	 */
	public static void setAttribute(String name,Object value){
		 getSession().setAttribute(name, value);
	}
	
	/**
	 * get Attribute from session by name
	 * @param name
	 * @return
	 */
	public static Object getAttribute(String name){
		return getSession().getAttribute(name);
	}
	
	/**
	 * remove Attribute from session by name
	 * @param name
	 */
	public static void removeAttribute(String name){
		 getSession().removeAttribute(name);
	}
	

	/**
	 * get Request Parameter by name
	 * @param name
	 * @return String
	 */
	public static String getParameter(String name){
		return getRequest().getParameter(name);
	}

}