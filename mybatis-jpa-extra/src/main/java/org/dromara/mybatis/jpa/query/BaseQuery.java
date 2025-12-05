/*
 * Copyright [2025] [MaxKey of copyright http://www.maxkey.top]
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
import java.util.List;

/**
 * BaseQuery
 */
public class BaseQuery {

    ArrayList<Condition> conditions = new ArrayList<>();
    
    ArrayList<Condition> groupBy ;
    
    ArrayList<Condition> orderBy ;
    
    boolean softDelete = true;
    
    public BaseQuery() {
        super();
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public List<Condition> getOrderBy() {
        return orderBy;
    }

    public List<Condition> getGroupBy() {
        return groupBy;
    }

    public boolean isSoftDelete() {
        return softDelete;
    }

    public void setSoftDelete(boolean softDelete) {
        this.softDelete = softDelete;
    }
     
}
