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
 

/**
 * 
 */
package org.dromara.mybatis.jpa.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @author Crystal
 *
 */
public class InstanceUtil {

    public static Object newInstance(String className) {
        Class<?> cls;
        try {
            cls = Class.forName(className);
            Constructor<?> constructor = cls.getConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static Object newInstance(Class<?> cls) {
        try {
            Constructor<?> constructor = cls.getConstructor();
            return constructor.newInstance();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @SuppressWarnings("rawtypes")
    public static Object newInstance(String className, Object[] args) {
        Class<?> newClass;
        try {
            newClass = Class.forName(className);
            Class[] argsClass = new Class[args.length];

            for (int i = 0, j = args.length; i < j; i++) {
                argsClass[i] = args[i].getClass();
            }

            Constructor<?> cons = newClass.getConstructor(argsClass);
            return cons.newInstance(args);
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return null;

    }

    @SuppressWarnings("rawtypes")
    public static <T> Object newInstance(Class<T> cls, Object[] args) {
        try {
            Class[] argsClass = new Class[args.length];

            for (int i = 0, j = args.length; i < j; i++) {
                argsClass[i] = args[i].getClass();
            }

            Constructor<T> cons = cls.getConstructor(argsClass);
            return cons.newInstance(args);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }
    

    @SuppressWarnings("rawtypes")
    public static Object invokeMethod(Object bean, String methodName,Object[] args) throws Exception {
        Class<? extends Object> beanClass = bean.getClass();
        Class[] argsClass = new Class[args.length];
        for (int i = 0, j = args.length; i < j; i++) {
            argsClass[i] = args[i].getClass();
        }

        Method method = beanClass.getMethod(methodName, argsClass);
        return method.invoke(bean, args);
    }

    public static Object invokeMethod(Object bean, String methodName)throws Exception {
        Class<? extends Object> beanClass = bean.getClass();
        Method method = beanClass.getMethod(methodName);
        return method.invoke(bean, new Object[] {});
    }

    @SuppressWarnings("rawtypes")
    public static Object invokeStaticMethod(Class<?> beanClass, String methodName,Object[] args) throws Exception {
        Class[] argsClass = new Class[args.length];
        for (int i = 0, j = args.length; i < j; i++) {
            argsClass[i] = args[i].getClass();
        }

        Method method = beanClass.getMethod(methodName, argsClass);
        return method.invoke(null, args);
    }

    public static Object invokeStaticMethod(Class<?> beanClass, String methodName)
            throws Exception {
        Method method = beanClass.getMethod(methodName);
        return method.invoke(null, new Object[] {});
    }
}
