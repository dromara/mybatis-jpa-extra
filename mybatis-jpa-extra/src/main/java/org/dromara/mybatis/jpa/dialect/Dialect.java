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
 
package org.dromara.mybatis.jpa.dialect;

import java.sql.PreparedStatement;

import org.dromara.mybatis.jpa.entity.JpaPage;

public interface Dialect {

    public static final String DEFAULT_BATCH_SIZE    =     "20";
    public static final String NO_BATCH              =     "0";
    
    public boolean supportsLimit();
    
    public String getLimitString(String query, JpaPage page) ;
    
    public String getPreparedStatementLimitString(String sql,  JpaPage pagination);
    
    public void setLimitParamters(PreparedStatement preparedStatement,int parameterSize,JpaPage page);
    
}
