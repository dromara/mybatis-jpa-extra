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
 
package org.dromara.mybatis.jpa.test.dao.service.impl;

import org.dromara.mybatis.jpa.repository.IJpaSqlRepository;
import org.dromara.mybatis.jpa.repository.impl.JpaSqlRepositoryImpl;
import org.dromara.mybatis.jpa.test.dao.persistence.ISqlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SqlRepositoryImpl extends JpaSqlRepositoryImpl implements IJpaSqlRepository {

	@Autowired
	private ISqlMapper mapper;
	
	@Override
	public ISqlMapper getMapper() {
		return mapper;
	}
	
}
