/*
 * Copyright [2026] [MaxKey of copyright http://www.maxkey.top]
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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ClassesUtils {

    /**
     * parseTypeArgs
     * @param classes
     * @return Type[]
     */
    public static Type[] parseTypeArgs(Class<?> classes) {
        Type[] interfaceParameterizedType = classes.getGenericInterfaces();
        if (interfaceParameterizedType != null && interfaceParameterizedType.length >= 1) {
            ParameterizedType parameterizedType = (ParameterizedType)interfaceParameterizedType[0];
            return parameterizedType.getActualTypeArguments();
        }
        return null;
    }
    
    /**
     * parseClassArgs
     * @param classes
     * @return Class<?>[]
     */
    public static Class<?>[] parseClassArgs(Class<?> classes) {
            Type[] typeArgs = parseTypeArgs(classes);
            return typeArgsToClassArgs(typeArgs);
    }
    
    /**
     * parseSuperTypeArgs
     * @param classes
     * @return Type[]
     */
    public static Type[] parseSuperTypeArgs(Class<?> classes) {
            return ((ParameterizedType) classes.getGenericSuperclass()).getActualTypeArguments();
    }
    
    /**
     * parseSuperClassArgs
     * @param classes
     * @return Class<?>[]
     */
    public static Class<?>[] parseSuperClassArgs(Class<?> classes) {
            Type[] typeArgs = parseSuperTypeArgs(classes);
        return typeArgsToClassArgs(typeArgs);
    }
    
    /**
     * typeArgsToClassArgs
     * @param typeArgs
     * @return Class<?>[]
     */
    public static Class<?>[] typeArgsToClassArgs(Type[] typeArgs) {
            Class<?>[] classArgs = new Class<?>[typeArgs.length];
        for(int i = 0 ;i < typeArgs.length ; i ++) {
            classArgs[i] = (Class<?>)typeArgs[i];
        }
        return classArgs;
    }
}
