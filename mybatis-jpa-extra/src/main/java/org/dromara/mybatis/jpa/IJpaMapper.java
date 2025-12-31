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
 

package org.dromara.mybatis.jpa;

import org.dromara.mybatis.jpa.mapper.IJpaFetchMapper;
import org.dromara.mybatis.jpa.mapper.IJpaFindMapper;
import org.dromara.mybatis.jpa.mapper.IJpaSoftDeleteMapper;
import org.dromara.mybatis.jpa.mapper.IJpaCrudMapper;

/**
 * IJpa Mapper
 * @author Crystal.sea
 * @param <T>
 */
public interface IJpaMapper<T> extends  IJpaCrudMapper<T>,
                                        IJpaFetchMapper<T>,
                                        IJpaFindMapper<T>,
                                        IJpaSoftDeleteMapper<T>
                                        {
            
}
