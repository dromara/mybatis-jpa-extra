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
 

package org.dromara.mybatis.jpa.query;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.dromara.mybatis.jpa.handler.SafeValueHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConditionValue {
    private static final Logger logger = LoggerFactory.getLogger(ConditionValue.class);
    
    public static String valueOfIterator(Iterator<?> iterator) {
        StringBuilder conditionArray = new StringBuilder();
        boolean isFirst = true;
        while (iterator.hasNext()) {
            Object element = iterator.next();
            if (!isFirst) {
                conditionArray.append(" , ");
            }
            conditionArray.append(SafeValueHandler.valueOfType(element));
            isFirst = false;
        }
        return conditionArray.toString();
    }
    
    public static String getCollectionValues(Object value) {
        if (value == null) {
            return "";
        }
        String collectionValues = "";
        String canonicalName = value.getClass().getCanonicalName();
        logger.trace("expression Class() {} , getSimpleName {} , value {}",canonicalName,value.getClass().getSimpleName(),value);
        if (value.getClass().isArray()) {
            Object[] objects = (Object[]) value;
            if (objects.length == 0) {
                return "";
            }
            Object firstElement = objects[0];
            if (firstElement != null) {
                if(firstElement instanceof Collection<?> cObjects) {//如果数组的第一个元素本身是个集合
                    logger.trace("objects[0] is Collection {}" , cObjects);
                    collectionValues = ConditionValue.valueOfIterator(cObjects.iterator());
                }else if(firstElement.getClass().isArray()) {//如果数组的第一个元素本身也是个数组
                    objects = (Object[])firstElement;
                    logger.trace("objects[0] is isArray {}" , objects);
                    collectionValues = ConditionValue.valueOfIterator(Arrays.asList(objects).iterator());
                }else {
                    //普通的一维数组
                    logger.trace("not  isArray");
                    collectionValues = ConditionValue.valueOfIterator(Arrays.asList(objects).iterator());
                }
            }
        }else if(value instanceof Collection <?> cObjects) {
            collectionValues = ConditionValue.valueOfIterator(cObjects.iterator());
        }
        return collectionValues;
    }
}