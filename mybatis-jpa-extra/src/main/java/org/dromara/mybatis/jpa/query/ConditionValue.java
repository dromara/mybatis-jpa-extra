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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.dromara.mybatis.jpa.handler.SafeValueHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConditionValue {
	private static final Logger logger = LoggerFactory.getLogger(ConditionValue.class);
	
	public static String valueOfList(List<?> listValue) {
		StringBuilder conditionArray = new StringBuilder();
		for (Object value : listValue) {
			if (!conditionArray.isEmpty()) {
				conditionArray.append(" , ");
			}
			conditionArray.append(SafeValueHandler.valueOfType(value));
		}
		return conditionArray.toString();
	}
	
	public static String valueOfArray(Object[] objects) {
		StringBuilder conditionArray = new StringBuilder();
		for (int i = 0 ; i< objects.length ; i++) {
			if (!conditionArray.isEmpty()) {
				conditionArray.append(" , ");
			}
			conditionArray.append(SafeValueHandler.valueOfType(objects[i]));
		}
		return conditionArray.toString();
	}
	
	public static String valueOfCollection(Collection<?> cObjects) {
		StringBuilder conditionArray = new StringBuilder();
        for (Object element : cObjects) {//for循环读取集合
        	if (!conditionArray.isEmpty()) {
				conditionArray.append(" , ");
			}
        	conditionArray.append(SafeValueHandler.valueOfType(element));
        }
		return conditionArray.toString();
	}
	
	public static String valueOfIterator(List<?> listValue) {
		StringBuilder conditionArray = new StringBuilder();
		Iterator<?> iterator = listValue.iterator();
        while (iterator.hasNext()) {//while循环读取集合
        	Object element = iterator.next();
        	if (!conditionArray.isEmpty()) {
				conditionArray.append(" , ");
			}
        	conditionArray.append(SafeValueHandler.valueOfType(element));
        }
		return conditionArray.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public static String getCollectionValues(Object value) {
		logger.trace("expression Class() {} , getSimpleName {}",value.getClass().getCanonicalName(),value.getClass().getSimpleName());
		String collectionValues = "";
		if (value.getClass().isArray()) {
			Object[] objects = (Object[]) value;
			if(objects[0] instanceof Collection<?> cObjects) {
				logger.trace("objects[0] is Collection {}" , cObjects);
				collectionValues = ConditionValue.valueOfCollection(cObjects);
			}else if(objects[0].getClass().isArray()) {
				objects = (Object[])objects[0];
				logger.trace("objects[0] is isArray {}" , objects);
				collectionValues = ConditionValue.valueOfArray(objects);
			}else {
				logger.trace("not  isArray {}" , objects);
				collectionValues = ConditionValue.valueOfArray(objects);
			}
		}else if(value.getClass().getCanonicalName().startsWith("java.util.ImmutableCollections")) {
			collectionValues = ConditionValue.valueOfIterator((List<?>) value);
		}else if(value.getClass().getCanonicalName().equalsIgnoreCase("java.util.ArrayList")) {
			collectionValues = ConditionValue.valueOfList((ArrayList) value);
		}else if(value.getClass().getCanonicalName().equalsIgnoreCase("java.util.LinkedList")) {
			collectionValues = ConditionValue.valueOfList((LinkedList) value);
		}
		return collectionValues;
	}
	
}