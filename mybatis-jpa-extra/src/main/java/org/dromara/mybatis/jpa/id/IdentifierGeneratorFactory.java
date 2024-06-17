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
 

package org.dromara.mybatis.jpa.id;

import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdentifierGeneratorFactory {
	
	private static final Logger logger 	= 	LoggerFactory.getLogger(IdentifierGeneratorFactory.class);
	
	static ConcurrentHashMap<String, IdentifierGenerator> identifierGeneratorMap = new ConcurrentHashMap<>();
	
	public IdentifierGeneratorFactory() {
		register(IdStrategy.UUID		, new UUIDGenerator());
		register(IdStrategy.SNOWFLAKEID	, new SnowFlakeIdGenerator());
		register(IdStrategy.DEFAULT		, new SnowFlakeIdGenerator(null));
	}
	
	public IdentifierGeneratorFactory(long datacenterId, long machineId) {
		register(IdStrategy.UUID, new UUIDGenerator());
		register(IdStrategy.SNOWFLAKEID, new SnowFlakeIdGenerator(datacenterId,machineId));
	}

	public static ConcurrentHashMap<String, IdentifierGenerator> getIdentifierGeneratorMap() {
		return identifierGeneratorMap;
	}

	public static void setIdentifierGeneratorMap(ConcurrentHashMap<String, IdentifierGenerator> identifierGeneratorMap) {
		IdentifierGeneratorFactory.identifierGeneratorMap = identifierGeneratorMap;
	}

	public void register(String strategy, IdentifierGenerator generator) {
		strategy = strategy.toLowerCase();
		if(IdentifierGeneratorFactory.identifierGeneratorMap.containsKey(strategy)) {
			return;
		}
		IdentifierGeneratorFactory.identifierGeneratorMap.put(strategy, generator);
		logger.debug( "Registering IdentifierGenerator strategy [{}] -> [{}]", strategy, generator.getClass().getName() );
	}
	
	public static boolean exists(String strategy) {
		return identifierGeneratorMap.containsKey(strategy.toLowerCase());
	}
	
	public static String generate(String strategy) {
		strategy = strategy.toLowerCase();
		return identifierGeneratorMap.get(strategy).generate(strategy);
	}

}
