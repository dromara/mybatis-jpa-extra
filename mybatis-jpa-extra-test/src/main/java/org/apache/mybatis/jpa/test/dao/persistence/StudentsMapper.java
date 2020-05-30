/**
 * 
 */
package org.apache.mybatis.jpa.test.dao.persistence;

import java.util.List;

import org.apache.mybatis.jpa.persistence.IJpaBaseMapper;
import org.apache.mybatis.jpa.test.domain.Students;


/**
 * @author Crystal.Sea
 *
 */

public  interface StudentsMapper extends IJpaBaseMapper<Students> {
	public List<Students> queryPageResults1(Students entity);
}
