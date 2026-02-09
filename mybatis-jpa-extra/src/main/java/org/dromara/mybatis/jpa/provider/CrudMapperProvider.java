/*
 * Copyright [2026] [MaxKey of copyright http://www.maxkey.top]
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
 

package org.dromara.mybatis.jpa.provider;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.mybatis.jpa.provider.impl.CountProvider;
import org.dromara.mybatis.jpa.provider.impl.DeleteProvider;
import org.dromara.mybatis.jpa.provider.impl.GetProvider;
import org.dromara.mybatis.jpa.provider.impl.InsertProvider;
import org.dromara.mybatis.jpa.provider.impl.QueryProvider;
import org.dromara.mybatis.jpa.provider.impl.UpdateProvider;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.query.Query;
import org.dromara.mybatis.jpa.update.LambdaUpdateWrapper;
import org.dromara.mybatis.jpa.update.UpdateWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Crystal.Sea
 *
 */
public class CrudMapperProvider <T extends JpaEntity,ID extends Serializable>{    
    static final Logger logger     =     LoggerFactory.getLogger(CrudMapperProvider.class);
    
    public CrudMapperProvider() {
        logger.trace("constructor init .");
    }

    //get
    public String get(Map<String, Object>  parametersMap) {
        return new GetProvider<>().get(parametersMap);  
    }
    
    //delete
    public String deleteById(Map<String, Object>  parametersMap) { 
        return new DeleteProvider<>().deleteById(parametersMap);  
    }  
    
    public String deleteBatch(Map<String, Object>  parametersMap) { 
        return new DeleteProvider<>().batchDelete(parametersMap);
    } 
    
    public String deleteByQuery(Class<?> entityClass,Query query) { 
        return new DeleteProvider<>().deleteByQuery(entityClass,query);
    } 
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public String deleteByLambdaQuery(Class<?> entityClass,LambdaQuery<T> lambdaQuery) { 
        return new DeleteProvider().deleteByLambdaQuery(entityClass,lambdaQuery);
    } 
    
    /**
     * @param entity
     * @return insert sql String
     */
    public String insert(T entity) {
        return new InsertProvider<>().insert(entity);
    }
    
    /**
     * @param List<T> entity
     * @return insert sql script
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public String insertBatch(List<T> listEntity) {
        return new InsertProvider().insertBatch(listEntity);
    }

    //update
    /**
     * @param entity
     * @return update sql String
     */
    public String update(T entity) {
        return new UpdateProvider<>().update(entity);
    }

    public String updateByQuery(Class<?> entityClass,String setSql,Query query) {
        return new UpdateProvider<>().updateByQuery(entityClass,setSql,query);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public String updateByLambdaQuery(Class<?> entityClass,String setSql,LambdaQuery<T> lambdaQuery) {
        return new UpdateProvider().updateByLambdaQuery(entityClass,setSql,lambdaQuery);
    }
    
    public String updateByUpdateWrapper(Class<?> entityClass,UpdateWrapper updateWrapper) {
        return new UpdateProvider<>().updateByUpdateWrapper(entityClass,updateWrapper);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public String updateByLambdaUpdateWrapper(Class<?> entityClass,LambdaUpdateWrapper<T> lambdaUpdateWrapper) {
        return new UpdateProvider().updateByLambdaUpdateWrapper(entityClass , lambdaUpdateWrapper);
    }
    
    //query
    public String query(T entity) {
        return new QueryProvider<>().query(entity);
    }
    
    public String queryByQuery(Class<?> entityClass,Query query) {
        return new QueryProvider<>().queryByQuery(entityClass,query);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public String queryByLambdaQuery(Class<?> entityClass,LambdaQuery<T> lambdaQuery) {
        return new QueryProvider().queryByLambdaQuery(entityClass,lambdaQuery);
    }
    
    //count
    public String countById(Class<?> entityClass,ID id) {
        return new CountProvider<>().countById(entityClass,id);
    }
    
    public String countByQuery(Class<?> entityClass,Query query) {
        return new CountProvider<>().countByQuery(entityClass,query);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public String countByLambdaQuery(Class<?> entityClass,LambdaQuery<T> lambdaQuery) {
        return new CountProvider().countByLambdaQuery(entityClass,lambdaQuery);
    }

}
