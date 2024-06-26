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


package org.dromara.mybatis.jpa.test.dao.service;

import java.util.List;

import org.dromara.mybatis.jpa.JpaService;
import org.dromara.mybatis.jpa.test.dao.persistence.StudentsMapper;
import org.dromara.mybatis.jpa.test.entity.Students;
import org.springframework.stereotype.Service;

@Service
public class StudentsService extends JpaService<Students> {

	public StudentsService() {
		super(StudentsMapper.class);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public StudentsMapper getMapper() {
		return (StudentsMapper) super.getMapper();
	}
	
	
	public List<Students> findByStdNo(String stdNo) {
		return getMapper().findByStdNo(stdNo);
	}
	
	public List<Students> findByStdMajorAndStdClass(String stdMajor,String stdClass) {
		return getMapper().findByStdMajorAndStdClass(stdMajor,stdClass);
	}
}
