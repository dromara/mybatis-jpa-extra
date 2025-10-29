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
 

package org.dromara.mybatis.jpa.interceptor;

import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.RowBounds;
import org.dromara.mybatis.jpa.constants.ConstMetaObject;
import org.dromara.mybatis.jpa.dialect.AbstractDialect;
import org.dromara.mybatis.jpa.dialect.Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractStatementHandlerInterceptor  implements Interceptor {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	protected Dialect 	dialect;
	
	protected String 	dialectString;

	/**
	 * @param dialect the dialect to set
	 */
	public void setDialect(AbstractDialect dialect) {
		logger.debug("dialect from bean : {}" , dialect);
		this.dialect = dialect;
	}

	
	/**
	 * @param dialectString the dialectString to set
	 */
	public void setDialectString(String dialectString) {
		this.dialectString = dialectString;
		try {
			logger.debug("dialect from String : {}" , dialectString);
			this.dialect =(AbstractDialect)Class.forName(dialectString).getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			logger.error("Dialect new error : " , e);
		} 
	}

	protected StatementHandler getStatementHandler(Invocation invocation) {
		StatementHandler statement = (StatementHandler) invocation.getTarget();
		if (statement instanceof RoutingStatementHandler) {
			MetaObject metaObject = SystemMetaObject.forObject(statement);
			return (StatementHandler)metaObject.getValue(ConstMetaObject.DELEGATE);
		
		}
		return statement;
	}
	
	protected RowBounds getRowBounds(StatementHandler statement) {
			MetaObject metaObject = SystemMetaObject.forObject(statement);
			return (RowBounds)metaObject.getValue(ConstMetaObject.ROWBOUNDS);
	}
	
	protected boolean hasBounds(RowBounds rowBounds) {
		return (rowBounds != null 
				&& rowBounds.getLimit() > 0 
				&& rowBounds.getLimit() < RowBounds.NO_ROW_LIMIT);
	}
	
}
