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
 

package org.dromara.mybatis.jpa.repository.impl;

import org.dromara.mybatis.jpa.IJpaMapper;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * JPA Base Repository
 * @author Crystal.Sea
 *
 * @param <T>
 */
public  class  JpaRepositoryImpl <M extends IJpaMapper<T>, T extends JpaEntity> extends AbstractJpaRepository<M,T>{
 
    @Autowired
    private M mapper;

    @Override
    public M getMapper() {
        return mapper;
    }
    
    @Override
    public void setMapper(M mapper) {
        this.mapper = mapper;
    }

    public JpaRepositoryImpl() {
        init();
    }
    
    public JpaRepositoryImpl(M mapper) {
        init();
        this.mapper = mapper;
    }
    
}
