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
 

/**
 * 
 */
package org.dromara.mybatis.jpa.provider;

import java.util.Map;

import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.mybatis.jpa.entity.JpaPage;
import org.dromara.mybatis.jpa.provider.base.DeleteProvider;
import org.dromara.mybatis.jpa.provider.base.FetchCountProvider;
import org.dromara.mybatis.jpa.provider.base.FetchProvider;
import org.dromara.mybatis.jpa.provider.base.FindProvider;
import org.dromara.mybatis.jpa.provider.base.GetProvider;
import org.dromara.mybatis.jpa.provider.base.InsertProvider;
import org.dromara.mybatis.jpa.provider.base.QueryProvider;
import org.dromara.mybatis.jpa.provider.base.SoftDeleteProvider;
import org.dromara.mybatis.jpa.provider.base.UpdateProvider;
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
@SuppressWarnings({ "unchecked", "rawtypes" })
public class MapperProvider <T extends JpaEntity>{    
    static final Logger logger     =     LoggerFactory.getLogger(MapperProvider.class);
    
    public MapperProvider() {
        logger.debug("constructor init .");
    }

    public String get(Map<String, Object>  parametersMap) {
        return new GetProvider().get(parametersMap);  
    }
    
    public String find(Map<String, Object>  parametersMap) throws Exception {
        return new FindProvider().find(parametersMap);  
    }
    
    public String findByIds(Map<String, Object>  parametersMap) {  
        return new FindProvider().findByIds(parametersMap);  
    }
    
    public String findAll(Map<String, Object>  parametersMap) {  
        return new FindProvider().findAll(parametersMap);  
    }
    
    //delete
    public String deleteById(Map<String, Object>  parametersMap) { 
        return new DeleteProvider().deleteById(parametersMap);  
    }  
    
    public String deleteBatch(Map<String, Object>  parametersMap) { 
        return new DeleteProvider().batchDelete(parametersMap);
    } 
    
    public String deleteByQuery(Class<?> entityClass,Query query) { 
        return new DeleteProvider().deleteByQuery(entityClass,query);
    } 
    
    public String deleteByLambdaQuery(Class<?> entityClass,LambdaQuery<T> lambdaQuery) { 
        return new DeleteProvider().deleteByLambdaQuery(entityClass,lambdaQuery);
    } 
    
    public String softDelete(Map<String, Object>  parametersMap) { 
        return new SoftDeleteProvider().softDelete(parametersMap);
    }
    
    public String softDeleteByQuery(Class<?> entityClass,Query query) { 
        return new SoftDeleteProvider().softDeleteByQuery(entityClass,query);
    } 
    
    public String softDeleteByLambdaQuery(Class<?> entityClass,LambdaQuery<T> lambdaQuery) { 
        return new SoftDeleteProvider().softDeleteByLambdaQuery(entityClass,lambdaQuery);
    } 
    
    /**
     * @param entity
     * @return insert sql String
     */
    public String insert(T entity) {
        return new InsertProvider().insert(entity);
    }

    //update
    /**
     * @param entity
     * @return update sql String
     */
    public String update(T entity) {
        return new UpdateProvider().update(entity);
    }

    public String updateByQuery(Class<?> entityClass,String setSql,Query query) {
        return new UpdateProvider().updateByQuery(entityClass,setSql,query);
    }
    
    public String updateByLambdaQuery(Class<?> entityClass,String setSql,LambdaQuery<T> lambdaQuery) {
        return new UpdateProvider().updateByLambdaQuery(entityClass,setSql,lambdaQuery);
    }
    
    public String updateByUpdateWrapper(Class<?> entityClass,UpdateWrapper updateWrapper) {
        return new UpdateProvider().updateByUpdateWrapper(entityClass,updateWrapper);
    }
    
    public String updateByLambdaUpdateWrapper(Class<?> entityClass,LambdaUpdateWrapper<T> lambdaUpdateWrapper) {
        return new UpdateProvider().updateByLambdaUpdateWrapper(entityClass , lambdaUpdateWrapper);
    }
    
    //fetch
    public String fetch(Map<String, Object>  parametersMap) {
        return new FetchProvider().fetch(parametersMap);
    }
    
    public String fetchByQuery(Map<String, Object>  parametersMap) {
        return new FetchProvider().fetchByQuery(parametersMap);
    }
    
    public String fetchByLambdaQuery(Map<String, Object>  parametersMap) {
        return new FetchProvider().fetchByLambdaQuery(parametersMap);
    }
    
    /**
     * @param entity
     * @return insert sql String
     */
    public String fetchCount(JpaPage entity) {
        return new FetchCountProvider().executeCount(entity);
    }
    
    
    //query
    public String query(T entity) {
        return new QueryProvider().query(entity);
    }
    
    public String queryByQuery(Class<?> entityClass,Query query) {
        return new QueryProvider().queryByQuery(entityClass,query);
    }
    
    public String queryByLambdaQuery(Class<?> entityClass,LambdaQuery<T> lambdaQuery) {
        return new QueryProvider().queryByLambdaQuery(entityClass,lambdaQuery);
    }
    
    
    public String countByQuery(Class<?> entityClass,Query query) {
        return new QueryProvider().countByQuery(entityClass,query);
    }
    
    public String countByLambdaQuery(Class<?> entityClass,LambdaQuery<T> lambdaQuery) {
        return new QueryProvider().countByLambdaQuery(entityClass,lambdaQuery);
    }

}
