/*
 * Copyright [2021] [MaxKey of copyright http://www.maxkey.top]
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
 

package org.dromara.mybatis.jpa.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.PropertyAccessorFactory;

public class BeanUtil {
	
	public static void copyBean(Object  origin,Object target) {
		if( origin == null || target == null) {
			return;
		}
		try {
			BeanUtils.copyProperties( origin, target);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
		
	public static Object cloneSupper(Object origin) {			
		Object target = null;
		if(origin == null) {
			return target;
		}
		try {				
			target = InstanceUtil.newInstance(origin.getClass().getSuperclass());
			BeanUtils.copyProperties(target,origin);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return target;
	}
		
	public static Object getValue(Object bean,String  field ) {
		if(bean == null) {
			return null;
		}
		try {
			return PropertyAccessorFactory
						.forBeanPropertyAccess(bean)
						.getPropertyValue(field);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean isNotNull(Map map) {
		return (map != null && map.size() > 0);
	}
	
	public static Object get(Object bean, String fieldName) {
		try {
			return invokeMethod(bean,getByProperty(fieldName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Object set(Object bean, String fieldName,Object value) {
		try {
			return invokeMethod(bean,setByProperty(fieldName),new Object[]{value});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Object getPublicProperty(Object bean, String fieldName) {
		try {
			Field field = bean.getClass().getField(fieldName);
			return field.get(bean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean setPublicProperty(Object bean, String fieldName,Object value) {
		try {
			bean.getClass().getField(fieldName).set(bean,value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static Boolean isPublicProperty(Class<? extends Object> cls, String fieldName) {
		return (isSetProperty(cls, fieldName)&& isGetProperty(cls, fieldName));
	}

	public static Boolean isPublicProperty(Object bean, String fieldName) {
		return isSetProperty(bean.getClass(), fieldName) && isGetProperty(bean.getClass(), fieldName);
	}

	public static <T> Boolean isSetProperty(Class<T> cls, String fieldName) {
		Method[] method = cls.getDeclaredMethods();
		for (int i = 0; i < method.length; i++) {
			if (method[i].getModifiers() == Modifier.PUBLIC
					&& method[i].getName().equals(setByProperty(fieldName))) {
				return true;
			}
		}
		return false;
	}

	public static <T> Boolean isGetProperty(Class<T> cls, String fieldName) {
		Method[] method = cls.getDeclaredMethods();
		for (int i = 0; i < method.length; i++) {
			if (method[i].getModifiers() == Modifier.PUBLIC
					&& method[i].getName().equals(getByProperty(fieldName))) {
				return true;
			}
		}
		return false;
	}

	public static Object newInstance(String className) {
		return InstanceUtil.newInstance(className);
	}
	
	public static <T> Object newInstance(Class<T> cls) {
		return InstanceUtil.newInstance(cls);
	}
	
	public static Object newInstance(String className, Object[] args) {
		return InstanceUtil.newInstance(className, args);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(Class<T> cls, Object[] args) {
		return (T) InstanceUtil.newInstance(cls, args);
	}
	public static Object invokeMethod(Object bean, String methodName, Object[] args)
	throws Exception {
		return InstanceUtil.invokeMethod(bean, methodName, args);
	}
	
	public static Object invokeMethod(Object bean, String methodName)
	throws Exception {
		return InstanceUtil.invokeMethod(bean, methodName);
	}

	public static Object invokeStaticMethod(Class<?> beanClass, String methodName,
			Object[] args) throws Exception {
		return InstanceUtil.invokeMethod(beanClass, methodName, args);
	}
	
	public static Object invokeStaticMethod(Class<?> beanClass, String methodName) throws Exception {
		return InstanceUtil.invokeStaticMethod(beanClass, methodName);
	}
	
	public static Map<String, String> getFields(Class<? extends Object> cls) {
		Field[] flds = cls.getDeclaredFields();
		Map<String, String> map = new HashMap<>();
		for (int i = 0; i < flds.length; i++) {
			map.put(flds[i].getName(), flds[i].getType().getName());
		}
		return map;
	}

	public static Map<String, String> getFields(Object bean)  {
		return getFields(bean.getClass());
	}

	public static Map<String, String> getPropertyFields(Class<? extends Object> cls){
		Field[] flds = cls.getDeclaredFields();
		Map<String, String> map = new HashMap<>();
		for (int i = 0; i < flds.length; i++) {
			String fieldName = flds[i].getName();
			if (isPublicProperty(cls, fieldName)) {
				map.put(flds[i].getName(), flds[i].getType().getName());
			}
		}
		return map;
	}

	public static Map<String, String> getPropertyFields(Object bean) {
		return getPropertyFields(bean.getClass());
	}
	
	public static <T> boolean isEmpty(T entity,Field field){
		return ! isNotEmpty(entity,field);
	}
	
	public static <T> boolean isNotEmpty(T entity,Field field){
		boolean isFieldNotEmpty=true;
		String fieldType=field.getType().getName();
		Object value=null;
		String fillValue=null;
		try {
			if(BeanUtil.get(entity, field.getName())==null){
				return false;
			}else{
				Object getValue = BeanUtil.get(entity, field.getName());
				fillValue = getValue == null? null : getValue.toString();
			}
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} 
		if(fieldType.equals("java.lang.String")){
			if(String.valueOf(fillValue)==null) {
				isFieldNotEmpty= false;
			}
        }else if(fieldType.equals("int")){
        	if(Integer.parseInt(fillValue)==0) {
        		isFieldNotEmpty= false;
        	}
        }else if(fieldType.equals("long")){
        	if(Long.parseLong(fillValue)==0) {
        		isFieldNotEmpty= false;
        	}
        }else if(fieldType.equals("java.lang.Long")){
        	if(Long.parseLong(fillValue)==0) {
        		isFieldNotEmpty= false;
        	}
        }else if(fieldType.equals("double")){
        	if(Double.valueOf(fillValue)==0.0d) {
        		isFieldNotEmpty= false;
        	}
        }else if(fieldType.equals("float")){
        	if(Float.parseFloat(fillValue)==0.0f) {
        		isFieldNotEmpty= false;
        	}
        }else if(fieldType.equals("java.util.Date")){ 
        	try {
        		value=BeanUtil.get(entity, field.getName());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} 
			if(value==null) {
				isFieldNotEmpty= false;
			}
        }else if(fieldType.equals("java.lang.Object")){
			value=BeanUtil.get(entity, field.getName());
			if(value==null) {
				isFieldNotEmpty= false;
			}
        }else if(fieldType.equals("char")){
        	if(fillValue !=null && Character.valueOf(fillValue.charAt(0))=='\u0000') {
        		isFieldNotEmpty= false;
        	}
        }else if(fieldType.equals("boolean")){
        	value= Boolean.parseBoolean(fillValue);
        }else if(fieldType.equals("short")){
        	if(Short.parseShort(fillValue)==0) {
        		isFieldNotEmpty= false;
        	}
        }else if(fieldType.equals("byte") && Byte.parseByte(fillValue)==0){
        	isFieldNotEmpty= false;
        }
		
		LogFactory.getLog(BeanUtil.class).debug("isFieldNotEmpty() fieldName : "+field.getName()+", fieldType : "+fieldType+", Value : "+fillValue+", isFieldNotEmpty : "+isFieldNotEmpty);
		
		return isFieldNotEmpty;
	}
	
	public static void displayValues(Object bean) {
		Field[] flds = bean.getClass().getDeclaredFields();
		LogFactory.getLog(BeanUtil.class).debug("displayValues() *******************************************");
		LogFactory.getLog(BeanUtil.class).debug("displayValues() "+bean.getClass().getName());
		for (int i = 0; i < flds.length; i++) {
			String name = flds[i].getName();
			if(isGetProperty(bean.getClass(),name)){
				LogFactory.getLog(BeanUtil.class).debug("displayValues() Field "+(i+1)+" : "+name+" = "+BeanUtil.get(bean, name));
			}
		}
		
		LogFactory.getLog(BeanUtils.class).debug("displayValues() *******************************************");
		
	}
	
	public static <T> void beanClone(T target,T origin){
		Field[] flds = target.getClass().getDeclaredFields();
		for (int i = 0; i < flds.length; i++) {
			String name = flds[i].getName();
			if(isPublicProperty(origin,name) && get(origin,name)!=null){
				set(target,name,get(origin,name));
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static Class[] getMethodParameterTypes(Class<?> c,String methodName){
		Method []methods=c.getMethods();
		for (Method method : methods) {
		  Class[] parameterTypes = method.getParameterTypes();
		  if(method.getName().equals(methodName)){
			  return parameterTypes;
		  }
		}
		return new Class[0];
	}
	
	public static String getByProperty(String property){
		String methodName="get"+( 
				property.length() == 1 ? 
						(Character.toUpperCase(property.charAt(0)) + "") : 
						 Character.toUpperCase(
								property.charAt(0))+ property.substring(1));
		LogFactory.getLog(BeanUtils.class).debug("getByProperty() methodName : "+methodName);
		return methodName;
	}
	
	public static String setByProperty(String property){
		String methodName="set"+( 
				property.length() == 1 ? 
						(Character.toUpperCase(property.charAt(0)) + "") : 
						 Character.toUpperCase(
								property.charAt(0))+ property.substring(1));
		LogFactory.getLog(BeanUtils.class).debug("setByProperty() methodName : "+methodName);
		return methodName;
	}
	
	
	public static <T> Map<String, Object> beanToMap( T  bean){
		Map <String,Object> mapBean=new HashMap<>();
		Field[] flds = bean.getClass().getDeclaredFields();
		LogFactory.getLog(BeanUtils.class).debug("beanToMap() *******************************************");
		LogFactory.getLog(BeanUtils.class).debug("beanToMap() "+bean.getClass().getName());
		for (int i = 0; i < flds.length; i++) {
			String fieldName = flds[i].getName();
			if(BeanUtil.isGetProperty(bean.getClass(),fieldName)){
				Object value=BeanUtil.get(bean, fieldName);
				mapBean.put(fieldName,value );
				LogFactory.getLog(BeanUtils.class).debug("beanToMap() field "+(i+1)+" : "+fieldName+" = "+value+" type : "+flds[i].getType());
			}
		}
		LogFactory.getLog(BeanUtils.class).debug("beanToMap() *******************************************");
		return mapBean;
	}
	@SuppressWarnings("rawtypes")
	public static <T> Object mapToBean(T bean,HashMap<?, ?> valueMap){
		Map<?, ?> beanFiledMap=null;
		try {
			beanFiledMap = BeanUtil.getFields(bean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(beanFiledMap==null) {
			return bean;
		}
		Iterator<?> fieldit = beanFiledMap.entrySet().iterator(); 
		LogFactory.getLog(BeanUtils.class).debug("mapToBean() *******************************************");
		LogFactory.getLog(BeanUtils.class).debug("mapToBean() "+bean.getClass().getName());
		int i=1;
        while (fieldit.hasNext()) {
            Map.Entry entry = (Map.Entry) fieldit.next();
            String fieldName = entry.getKey().toString();
            Object value = null;
            String fieldType=(String)beanFiledMap.get(fieldName);
            if(valueMap.get(fieldName)==null) {
            	continue;
            }
            String fillValue=valueMap.get(fieldName).toString();
            LogFactory.getLog(BeanUtils.class).debug("mapToBean() field "+(i++)+" : "+fieldName+" = "+fillValue+" type : "+fieldType);  
            if(fieldType.equals("java.lang.String")){
            	value=String.valueOf(fillValue);
            }else if(fieldType.equals("int")){
            	value=Integer.parseInt(fillValue);
            }else if(fieldType.equals("java.lang.Integer")){
            	value=Integer.parseInt(fillValue);
            }else if(fieldType.equals("long")){
            	value=Long.parseLong(fillValue);
            }else if(fieldType.equals("java.lang.Long")){
            	value= Long.parseLong(fillValue);
            }else if(fieldType.equals("double")){
            	value=(double)Double.valueOf(fillValue);
            }else if(fieldType.equals("java.lang.Double")){
            	value=Double.valueOf(fillValue);
            }else if(fieldType.equals("float")){
            	value=Float.parseFloat(fillValue);
            }else if(fieldType.equals("java.lang.Float")){
            	value=Float.parseFloat(fillValue);
            }else if(fieldType.equals("java.util.Date")){ 
            	try {
            		if(fillValue.length()==10){
            			fillValue+=" 00:00:00";
            			value=(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).parse(fillValue);
            		}else{
            			continue;
            		}
				} catch (ParseException e) {
					e.printStackTrace();
				}
            }else if(fieldType.equals("java.lang.Object")){
            	value=valueMap.get(fieldName);
            }else if(fieldType.equals("char")){
            	value=Character.valueOf(fillValue.charAt(0));
            }else if(fieldType.equals("boolean")){
            	value=Boolean.parseBoolean(fillValue);
            }else if(fieldType.equals("short")){
            	value=Short.parseShort(fillValue);
            }else if(fieldType.equals("byte")){
            	value=Byte.parseByte(fillValue);
            }

           BeanUtil.set(bean, fieldName, value);   
        }
		LogFactory.getLog(BeanUtils.class).debug("mapToBean() *******************************************");
		return bean;
	}
	
	public static boolean isFieldBlank(Object value) {
    	return (value == null || StringUtils.isBlank(value.toString()) || "null".equalsIgnoreCase(value.toString()));
    }
}
