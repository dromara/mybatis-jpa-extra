package org.apache.mybatis.jpa.id;

import java.util.UUID;

public class UUIDGenerator implements IdentifierGenerator{

	public String generate(Object object) {
		// TODO Auto-generated method stub
		return UUID.randomUUID().toString().toLowerCase();
	}

}
