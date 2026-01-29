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

package org.dromara.mybatis.jpa.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

/**
 * 动态路由数据源
 * 支持运行时动态添加、移除数据源
 */
@Component
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {
    private static final Logger logger = LoggerFactory.getLogger(DynamicRoutingDataSource.class);

    // 默认数据源key
    public static final String DEFAULT_DATASOURCE_KEY = "default";

    // 存储数据源的线程安全Map
    private static final Map<Object, Object> DATA_SOURCE_MAP = new ConcurrentHashMap<>();

    // 当前默认数据源key
    private static String defaultDataSourceKey = DEFAULT_DATASOURCE_KEY;

    /**
     * 获取当前数据源key
     * @return 当前数据源key
     */
    @Override
    protected Object determineCurrentLookupKey() {
        String currentKey = DynamicDataSourceContextHolder.getDataSource();
        // 如果未设置数据源，使用默认数据源
        if (StringUtils.isBlank(currentKey)) {
            currentKey = defaultDataSourceKey;
        }
        logger.debug("Determined current lookup key: {}", currentKey);
        return currentKey;
    }

    /**
     * 设置目标数据源Map
     * @param targetDataSources 目标数据源Map
     */
    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        if (ObjectUtils.isNotEmpty(targetDataSources)) {
            super.setTargetDataSources(targetDataSources);
            DATA_SOURCE_MAP.clear();
            DATA_SOURCE_MAP.putAll(targetDataSources);
            super.afterPropertiesSet();
            logger.info("Updated target data sources, total count: {}", targetDataSources.size());
        } else {
            logger.warn("Target data sources is null or empty");
        }
    }

    /**
     * 动态添加数据源
     * @param key 数据源key
     * @param dataSource 数据源实例
     * @return true如果添加成功，false如果key已存在
     */
    public boolean addDataSource(String key, DataSource dataSource) {
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("DataSource key cannot be null or empty");
        }
        if (dataSource == null) {
            throw new IllegalArgumentException("DataSource cannot be null");
        }

        if (DATA_SOURCE_MAP.containsKey(key)) {
            logger.warn("DataSource with key '{}' already exists, skipping registration.", key);
            return false;
        }

        // 测试数据源连接
        try (Connection conn = dataSource.getConnection()) {
            if (conn == null || conn.isClosed()) {
                throw new SQLException("Failed to obtain a valid connection for key: " + key);
            }
        } catch (SQLException e) {
            // 不在这里打 error，交给上层统一处理
            throw new RuntimeException("Invalid DataSource for key: " + key, e);
        }

        // 注册数据源
        DATA_SOURCE_MAP.put(key, dataSource);
        super.setTargetDataSources(new ConcurrentHashMap<>(DATA_SOURCE_MAP));
        super.afterPropertiesSet();

        logger.info("Successfully added DataSource with key: {}", key);
        return true;
    }

    /**
     * 移除数据源
     * @param key 数据源key
     * @return true如果移除成功，false如果key不存在
     */
    public boolean removeDataSource(String key) {
        if (StringUtils.isBlank(key)) {
        		logger.warn("DataSource key cannot be null or empty");
            return false;
        }

        if (key.equals(defaultDataSourceKey)) {
            logger.warn("Cannot remove default DataSource: {}", key);
            return false;
        }

        Object removed = DATA_SOURCE_MAP.remove(key);
        if (removed != null) {
            super.setTargetDataSources(new ConcurrentHashMap<>(DATA_SOURCE_MAP));
            super.afterPropertiesSet();
            logger.info("Successfully removed DataSource with key: {}", key);
            return true;
        } else {
            logger.warn("DataSource with key '{}' does not exist", key);
            return false;
        }
    }

    /**
     * 获取数据源Map的只读副本
     * @return 数据源Map的只读副本
     */
    public Map<Object, Object> getDataSourceMap() {
        return Collections.unmodifiableMap(DATA_SOURCE_MAP);
    }

    /**
     * 获取所有数据源key的集合
     * @return 数据源key集合的只读副本
     */
    public Set<Object> getDataSourceKeys() {
        return Collections.unmodifiableSet(DATA_SOURCE_MAP.keySet());
    }

    /**
     * 检查指定key的数据源是否存在
     * @param key 数据源key
     * @return true如果存在，false如果不存在
     */
    public static boolean isExist(Object key) {
        return key != null && DATA_SOURCE_MAP.containsKey(key);
    }

    /**
     * 获取数据源数量
     * @return 数据源数量
     */
    public static int getDataSourceCount() {
        return DATA_SOURCE_MAP.size();
    }

    /**
     * 设置默认数据源key
     * @param key 默认数据源key
     */
    public static void setDefaultDataSourceKey(String key) {
        if (StringUtils.isNotBlank(key)) {
            defaultDataSourceKey = key;
            logger.info("Set default DataSource key to: {}", key);
        }
    }

    /**
     * 获取默认数据源key
     * @return 默认数据源key
     */
    public static String getDefaultDataSourceKey() {
        return defaultDataSourceKey;
    }

    /**
     * 检查数据源健康状态
     * @param key 数据源key
     * @return true如果数据源健康，false如果不健康
     */
    public boolean checkDataSourceHealth(String key) {
        Object dataSourceObj = DATA_SOURCE_MAP.get(key);
        if (!(dataSourceObj instanceof DataSource dataSource)) {
            return false;
        }

        try {
            dataSource.getConnection().close();
            logger.debug("DataSource '{}' health check passed", key);
            return true;
        } catch (SQLException e) {
            logger.error("DataSource '{}' health check failed", key, e);
            return false;
        }
    }
}
