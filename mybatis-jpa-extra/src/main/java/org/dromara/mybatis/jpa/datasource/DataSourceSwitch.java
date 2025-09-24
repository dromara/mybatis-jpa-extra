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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 切换当前数据源工具类
 * 提供数据源切换、获取、清理等操作
 */
public class DataSourceSwitch {
	private static final Logger logger = LoggerFactory.getLogger(DataSourceSwitch.class);

	/**
	 * 切换数据源
	 * @param key 数据源key
	 * @return 当前生效的数据源key
	 * @throws IllegalArgumentException 当数据源key为空时抛出
	 */
	public static String change(String key) {
		if (StringUtils.isBlank(key)) {
			throw new IllegalArgumentException("DataSource key cannot be null or empty");
		}

		String currentKey = DynamicDataSourceContextHolder.getDataSource();
		logger.debug("Current DataSource: {}, Target DataSource: {}", currentKey, key);

		// 如果目标数据源与当前数据源相同，无需切换
		if (key.equals(currentKey)) {
			logger.trace("DataSource {} is already active, no change needed", key);
			return currentKey;
		}

		// 检查目标数据源是否存在
		if (!DynamicRoutingDataSource.isExist(key)) {
			logger.error("DataSource key '{}' does not exist in the configured data sources", key);
			throw new IllegalArgumentException("DataSource key '" + key + "' does not exist");
		}

		DynamicDataSourceContextHolder.setDataSource(key);
		logger.info("Successfully switched DataSource from '{}' to '{}'", currentKey, key);
		return key;
	}

	/**
	 * 切换到默认数据源
	 * @return 默认数据源key，如果没有配置则返回null
	 */
	public static String switchToDefault() {
		String defaultKey = DynamicRoutingDataSource.getDefaultDataSourceKey();
		DynamicDataSourceContextHolder.clearDataSource();
		logger.info("Switched to default DataSource: {}", defaultKey);
		return defaultKey;
	}

	/**
	 * 获取当前数据源key
	 * @return 当前数据源key
	 */
	public static String getCurrentDataSource() {
		return DynamicDataSourceContextHolder.getDataSource();
	}

	/**
	 * 检查指定数据源是否存在
	 * @param key 数据源key
	 * @return true如果存在，false如果不存在
	 */
	public static boolean exists(String key) {
		return DynamicRoutingDataSource.isExist(key);
	}
}
