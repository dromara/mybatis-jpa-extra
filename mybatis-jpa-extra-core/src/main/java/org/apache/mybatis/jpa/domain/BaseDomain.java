package org.apache.mybatis.jpa.domain;

import java.io.Serializable;
import java.util.UUID;

/**
 * BaseDomain for Database Table domain
 * 
 * @author Crystal.sea
 * 
 */
public class BaseDomain extends Pagination implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6290127045507211154L;
	
	public String generateId() {
		return UUID.randomUUID().toString().toLowerCase();
	}
}
