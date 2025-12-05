/*
 * Copyright [2024] [MaxKey of copyright http://www.maxkey.top]
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

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.dromara.mybatis.jpa.functions.IGetter;

import jakarta.persistence.Column;

public class LambdaUtil {
    public static final String PREFIX_GET        = "get";

    public static final String PREFIX_IS         = "is";
    
    public static final String WRITE_REPLACE     = "writeReplace";
    

    /**
     * Entity::getName -> name
     *
     * @param func 方法getter函数
     * @param <T> 类型
     * @return 返回字段名
     */
    public static <T> String getColumnName(IGetter<T> func) {
        SerializedLambda lambda = getSerializedLambda(func);
        String methodName = lambda.getImplMethodName();
        if (methodName.startsWith(PREFIX_GET)) {
            methodName = methodName.substring(3);
        } else if (methodName.startsWith(PREFIX_IS)) {
            methodName = methodName.substring(2);
        }
        String fieldName = StringUtils.uncapitalize(methodName);
        String implClass = lambda.getImplClass();
        Class<?> clazz = forName(implClass);
        Field field = findField(clazz, fieldName);
        return getColumnName(field, fieldName);
    }

    private static String getColumnName(Field field, String fieldName) {
        return Optional.ofNullable(field)
                .map(f -> f.getAnnotation(Column.class))
                .map(Column::name)
                .filter(name -> !"".equals(name))
                .orElseGet(() -> fieldName);
    }
    
    public static SerializedLambda getSerializedLambda(Serializable func) {
        Class<? extends Serializable> clazz = func.getClass();
        try {
            Method method = clazz.getDeclaredMethod(WRITE_REPLACE);
            method.setAccessible(Boolean.TRUE);
            return (SerializedLambda) method.invoke(func);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("can't parser getter name of " + clazz.getClass().getSimpleName(), e);
        }
    }
    
    public static Class<?> forName(String impClass) {
        try {
            return Class.forName(impClass.replace("/", "."));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("ClassNotFoundException class name of " + impClass, e);
        }
    }
    
    public static Field findField(Class<?> clazz, String fieldName) {
        if (StringUtils.isNotBlank(fieldName)) {
            Class<?> searchType = clazz;
            while (Object.class != searchType && searchType != null) {
                Field[] fields = searchType.getDeclaredFields();
                for (Field field : fields) {
                    if (fieldName.equals(field.getName())) {
                        return field;
                    }
                }
                searchType = searchType.getSuperclass();
            }
        }
        return null;
    }
}
