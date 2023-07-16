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
 

package org.dromara.mybatis.jpa.entity;

import java.io.Serializable;
import java.util.UUID;

import org.dromara.mybatis.jpa.id.IdStrategy;
import org.dromara.mybatis.jpa.id.IdentifierGeneratorFactory;
import org.dromara.mybatis.jpa.metadata.MapperMetadata;

/**
 * Base Entity for Database Table Entity
 * 
 * @author Crystal.sea
 * 
 */
public class JpaEntity extends JpaPagination implements Serializable{

	private static final long serialVersionUID = -6984977786868857466L;

	public String generateId() {
		if(MapperMetadata.identifierGeneratorFactory != null) {
			return IdentifierGeneratorFactory.generate(IdStrategy.DEFAULT);
		}else {
			return UUID.randomUUID().toString().toLowerCase();
		}
	}
}
