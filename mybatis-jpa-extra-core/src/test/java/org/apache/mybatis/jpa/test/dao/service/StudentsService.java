package org.apache.mybatis.jpa.test.dao.service;

import org.apache.mybatis.jpa.persistence.JpaBaseService;
import org.apache.mybatis.jpa.test.dao.persistence.StudentsMapper;
import org.apache.mybatis.jpa.test.domain.Students;
import org.springframework.stereotype.Service;


@Service
public class StudentsService extends JpaBaseService<Students> {

	public StudentsService() {
		super(StudentsMapper.class);

	}

	/* (non-Javadoc)
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public StudentsMapper getMapper() {
		// TODO Auto-generated method stub
		return (StudentsMapper)super.getMapper();
	}
	


}
