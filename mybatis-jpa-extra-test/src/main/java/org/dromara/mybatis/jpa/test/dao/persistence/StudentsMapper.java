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

/**
 * 
 */
package org.dromara.mybatis.jpa.test.dao.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.dromara.mybatis.jpa.IJpaMapper;
import org.dromara.mybatis.jpa.test.entity.Students;

/**
 * @author Crystal.Sea
 *
 */

public interface StudentsMapper extends IJpaMapper<Students> {
	
	public List<Students> fetchPageResults1(Students entity);
	
	public List<Students> fetchPageResults(Students entity);
	
	@Select({})
	public List<Students> findByStdNo(String stdNo);
	
	@Select({})
	public List<Students> findByStdNoIs(String stdNo);
	
	@Select({})
	public List<Students> findByStdNoEquals(String stdNo);
	
	@Select({})
	public List<Students> findByStdAgeBetween(int ageStart,int ageEnd);
	
	@Select({})
	public List<Students> findByStdAgeLessThan(int ageLessThan);
	
	@Select({})
	public List<Students> findByStdAgeLessThanEqual(int ageLessThanEqual);
	
	@Select({})
	public List<Students> findByStdAgeAfter(int ageAfter);
	
	@Select({})
	public List<Students> findByStdAgeBefore(int ageBefore);
	
	@Select({})
	public List<Students> findByImagesNull();
	
	@Select({})
	public List<Students> findByImagesIsNull();
	
	@Select({})
	public List<Students> findByImagesIsNotNull();
	
	@Select({})
	public List<Students> findByImagesNotNull();
	
	@Select({})
	public List<Students> findByStdNameLike(String stdName);
	
	@Select({})
	public List<Students> findByStdNameNotLike(String stdName);
	
	@Select({})
	public List<Students> findByStdNameStartingWith(String stdName);
	
	@Select({})
	public List<Students> findByStdNameEndingWith(String stdName);
	
	@Select({})
	public List<Students> findByStdNameContaining(String stdName);
	
	@Select({})
	public List<Students> findByStdGenderOrderByStdAge(String stdGender);
	
	@Select({})
	public List<Students> findByStdGenderIsOrderByStdAge(String stdGender);
	
	@Select({})
	public List<Students> findByStdMajorIn(String... stdMajors) ;
	
	@Select({})
	public List<Students> findByStdMajorNotIn(List<String> stdMajors);
	
	@Select({})
	public List<Students> findByDeletedTrue();
	
	@Select({})
	public List<Students> findByDeletedFalse();
	
	@Select({})
	public List<Students> findByStdGenderIgnoreCase(String stdGender);
	
	@Select({})
	public List<Students> findByStdNoNot(String stdNo);
	
	@Select({})
	public List<Students> findByStdMajorAndStdClass(String stdMajor,String stdClass);
	
	
	@Update({"update Students set password = #{password} where stdNo = #{stdNo}"})
	public int updatePassword(Students std);
}
