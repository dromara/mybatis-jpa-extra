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
 

package org.apache.mybatis.jpa.id;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdentifierGeneratorFactory {
	private static final Logger _logger 	= 	LoggerFactory.getLogger(IdentifierGeneratorFactory.class);
	public static ConcurrentHashMap<String, IdentifierGenerator> generatorStrategyMap = new ConcurrentHashMap<String, IdentifierGenerator>();
	
	public IdentifierGeneratorFactory() {
		register("uuid", new UUIDGenerator());
		register("uuid.hex", new UUIDHexGenerator());
		register("snowflakeid", new SnowFlakeIdGenerator());
	}
	
	public IdentifierGeneratorFactory(long datacenterId, long machineId) {
		register("uuid", new UUIDGenerator());
		register("uuid.hex", new UUIDHexGenerator());
		register("snowflakeid", new SnowFlakeIdGenerator(datacenterId,machineId));
	}

	public ConcurrentHashMap<String, IdentifierGenerator> getGeneratorStrategyMap() {
		return generatorStrategyMap;
	}

	public void setGeneratorStrategyMap(ConcurrentHashMap<String, IdentifierGenerator> generatorStrategyMap) {
		for (Map.Entry<String, IdentifierGenerator> entry : generatorStrategyMap.entrySet()) {  
			register(entry.getKey(),entry.getValue());
		}  
	}
	
	public void register(String strategy, IdentifierGenerator generator) {
		if(IdentifierGeneratorFactory.generatorStrategyMap.containsKey(strategy)) {
			return;
		}
		IdentifierGeneratorFactory.generatorStrategyMap.put(strategy, generator);
		_logger.debug( "Registering IdentifierGenerator strategy [{}] -> [{}]", strategy, generator.getClass().getName() );
	}
	
	public String generate(String strategy) {
		return generatorStrategyMap.get(strategy).generate(null);
	}

}
