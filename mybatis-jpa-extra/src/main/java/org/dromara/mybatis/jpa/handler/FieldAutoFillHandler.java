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
 

package org.dromara.mybatis.jpa.handler;

import java.util.Objects;

import org.apache.ibatis.reflection.MetaObject;

public abstract class FieldAutoFillHandler {
    
    public abstract void insertFill(MetaObject metaObject) ;
    

    public abstract void updateFill(MetaObject metaObject) ;
    
    
    protected FieldAutoFillHandler setFieldValue(MetaObject metaObject , String field, Object value) {
        if (Objects.nonNull(value) && metaObject.hasSetter(field)) {
            metaObject.setValue(field, value);
        }
        return this;
    }
    
}
