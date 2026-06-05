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
 

package org.dromara.mybatis.jpa.exceptions;

import org.apache.ibatis.exceptions.PersistenceException;

/**
 * MybatisJpaException
 */
public class MybatisJpaException  extends PersistenceException {

    /**
     * 
     */
    private static final long serialVersionUID = -3852001627095126024L;

    public MybatisJpaException(String message) {
        super(message);
    }

    public MybatisJpaException(String message, Throwable cause) {
        super(message, cause);
    }

    public MybatisJpaException(Throwable cause) {
        super(cause);
    }

}
