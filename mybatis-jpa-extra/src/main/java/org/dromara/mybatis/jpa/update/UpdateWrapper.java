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
 

package org.dromara.mybatis.jpa.update;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dromara.mybatis.jpa.query.Condition;
import org.dromara.mybatis.jpa.query.Operator;
import org.dromara.mybatis.jpa.query.Query;

public class UpdateWrapper extends Query{
    
    List<Condition> sets = new ArrayList<>();
    
    public UpdateWrapper set(String column ,Object value) {
        if (StringUtils.isBlank(column)) {
            throw new IllegalArgumentException("Failed to resolve column from expression");
        }
        sets.add(new Condition(Operator.SET,column,value));
        return  this;
    }

    public List<Condition> getSets() {
        return Collections.unmodifiableList(sets);
    }

    @Override
    public String toString() {
        return "UpdateWrapper [sets=" + sets + "]";
    }
    
}
