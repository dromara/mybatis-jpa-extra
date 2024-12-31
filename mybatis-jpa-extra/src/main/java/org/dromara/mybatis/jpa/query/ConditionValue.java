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

import java.util.Collection;
import java.util.List;

import org.dromara.mybatis.jpa.handler.SafeValueHandler;

public class ConditionValue {

	public static String valueOfList(List<?> listValue) {
		StringBuffer conditionArray = new StringBuffer();
		for (Object value : listValue) {
			if (conditionArray.length() > 0) {
				conditionArray.append(" , ");
			}
			conditionArray.append(SafeValueHandler.valueOfType(value));
		}
		return conditionArray.toString();
	}
	
	public static String valueOfArray(Object[] objects) {
		StringBuffer conditionArray = new StringBuffer();
		for (int i = 0 ; i< objects.length ; i++) {
			if (conditionArray.length() > 0) {
				conditionArray.append(" , ");
			}
			conditionArray.append(SafeValueHandler.valueOfType(objects[i]));
		}
		return conditionArray.toString();
	}
	
	public static String valueOfCollection(Collection<?> cObjects) {
		StringBuffer conditionArray = new StringBuffer();
        for (Object element : cObjects) {//for循环读取集合
        	if (conditionArray.length() > 0) {
				conditionArray.append(" , ");
			}
        	conditionArray.append(SafeValueHandler.valueOfType(element));
        }
		return conditionArray.toString();
	}
	
}