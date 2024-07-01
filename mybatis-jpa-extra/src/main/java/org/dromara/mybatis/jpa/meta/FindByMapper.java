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
 

package org.dromara.mybatis.jpa.meta;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.dromara.mybatis.jpa.query.JpaFindByKeywords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FindByMapper {
	private static final Logger logger 	= 	LoggerFactory.getLogger(FindByMapper.class);
	
	/**
	 * 
	 */
	String mappedStatementId;
	
	/**
	 * 
	 */
	String mappedStatementMethodName;
	
	/**
	 * 
	 */
	String removedFindByName;
	
	/**
	 * 
	 */
	Class<?> mappedStatementClass;
	
	String mappedStatementClassName;
	/**
	 * 
	 */
	Class<?> entityClass;
	
	boolean isDistinct = false;
	
	boolean isFindBy = false;

	public FindByMapper(String mappedStatementId) {
		this.mappedStatementId = mappedStatementId;
	}
	
	public void parseFindBy() {
		mappedStatementClassName = mappedStatementId.substring(0, mappedStatementId.lastIndexOf("."));
		logger.trace("mappedStatementClass {}" ,mappedStatementClassName);
		mappedStatementMethodName = mappedStatementId.substring(mappedStatementId.lastIndexOf(".") + 1);
		logger.trace("methodName {}" , mappedStatementMethodName);

		if(mappedStatementMethodName.startsWith(JpaFindByKeywords.FINDBY)) {
			isFindBy = true;
			removedFindByName = mappedStatementMethodName.substring(JpaFindByKeywords.FINDBY.length());
		}else if(mappedStatementMethodName.startsWith(JpaFindByKeywords.FINDDISTINCTBY)){
			isFindBy = true;
			isDistinct = true;
			removedFindByName = mappedStatementMethodName.substring(JpaFindByKeywords.FINDDISTINCTBY.length());
		}
		logger.trace("removed FindBy name : {}" , removedFindByName);
	}
	
	public void parseEntityClass() {
		try {
			mappedStatementClass  = Class.forName(mappedStatementClassName);
			Type[] pType = mappedStatementClass.getGenericInterfaces();
			if (pType != null && pType.length >= 1) {
				ParameterizedType parameterizedType = (ParameterizedType)pType[0];
				if(parameterizedType != null && parameterizedType.getActualTypeArguments().length > 0) {
					entityClass = (Class<?>)parameterizedType.getActualTypeArguments()[0];
					logger.trace("Entity Class : {}" , entityClass.getCanonicalName());
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public String getMappedStatementId() {
		return mappedStatementId;
	}

	public String getMappedStatementMethodName() {
		return mappedStatementMethodName;
	}

	public String getRemovedFindByName() {
		return removedFindByName;
	}

	public Class<?> getMappedStatementClass() {
		return mappedStatementClass;
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public boolean isDistinct() {
		return isDistinct;
	}

	public boolean isFindBy() {
		return isFindBy;
	}

}
