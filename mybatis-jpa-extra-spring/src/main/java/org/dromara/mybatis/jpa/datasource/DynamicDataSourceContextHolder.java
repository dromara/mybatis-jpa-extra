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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据源上下文持有者
 * 使用ThreadLocal保证线程安全性，为每个线程维护独立的数据源上下文
 */
public class DynamicDataSourceContextHolder {
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceContextHolder.class);

    /**
     * 为每个线程维护数据源key变量，避免线程间互相影响
     */
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 设置当前线程的数据源
     *
     * @param key 数据源key
     */
    public static void setDataSource(String key) {
        if (key != null) {
            CONTEXT_HOLDER.set(key);
            logger.debug("Set DataSource for current thread: {}", key);
        } else {
            logger.warn("Attempted to set null DataSource key");
        }
    }

    /**
     * 获取当前线程的数据源
     *
     * @return 数据源key，如果未设置则返回null
     */
    public static String getDataSource() {
        String key = CONTEXT_HOLDER.get();
        logger.trace("Get DataSource for current thread: {}", key);
        return key;
    }

    /**
     * 清除当前线程的数据源设置，恢复为默认数据源
     */
    public static void clearDataSource() {
        String currentKey = CONTEXT_HOLDER.get();
        CONTEXT_HOLDER.remove();
        logger.debug("Cleared DataSource for current thread, was: {}", currentKey);
    }

    /**
     * 检查当前线程是否设置了数据源
     *
     * @return true如果已设置数据源，false如果未设置
     */
    public static boolean hasDataSource() {
        return CONTEXT_HOLDER.get() != null;
    }
}
