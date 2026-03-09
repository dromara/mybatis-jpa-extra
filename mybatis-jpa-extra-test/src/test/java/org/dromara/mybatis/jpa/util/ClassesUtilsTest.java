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

import java.lang.reflect.Type;

import org.junit.jupiter.api.Test;

public class ClassesUtilsTest {

    @Test
    public  void parseTypeArgsTest() throws ClassNotFoundException {
        String className = "org.dromara.mybatis.jpa.test.dao.persistence.StudentsMapper";
        Class<?> classes  = Class.forName(className);
        Type[] types  =ClassesUtils.parseTypeArgs(classes);
        for(int i=0;i<types.length;i++) {
            System.out.println("types "+i+" " +types[i].getTypeName());
        }
     }
    
    @Test
    public  void parseClassArgsTest() throws ClassNotFoundException {
        String className = "org.dromara.mybatis.jpa.test.dao.persistence.StudentsMapper";
        Class<?> classes  = Class.forName(className);
        Class<?>[] classArgs  =ClassesUtils.parseClassArgs(classes);
        for(int i=0;i<classArgs.length;i++) {
            System.out.println("Class "+i+" " +classArgs[i].getCanonicalName());
        }
     }
    
    
    @Test
    public  void parseImplTypeArgsTest() throws ClassNotFoundException {
        String className = "org.dromara.mybatis.jpa.test.dao.service.impl.StudentsServiceImpl";
        Class<?> classes  = Class.forName(className);
        Type[] types  =ClassesUtils.parseSuperTypeArgs(classes);
        for(int i=0;i<types.length;i++) {
            System.out.println("types "+i+" " +types[i].getTypeName());
        }
     }
    
    @Test
    public  void parseImplClassArgsTest() throws ClassNotFoundException {
        String className = "org.dromara.mybatis.jpa.test.dao.service.impl.StudentsServiceImpl";
        Class<?> classes  = Class.forName(className);
        Class<?>[] classArgs  = ClassesUtils.parseSuperClassArgs(classes);
        for(int i=0;i<classArgs.length;i++) {
            System.out.println("Class "+i+" " +classArgs[i].getTypeName());
        }
     }
}
