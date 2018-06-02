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
		//register("serial", new SerialGenerator());
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
